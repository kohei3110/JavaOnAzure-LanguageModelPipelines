package com.koheisaito.sk4j.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatMessage;
import com.azure.ai.openai.models.ChatRole;
import com.azure.core.credential.AzureKeyCredential;
import com.koheisaito.sk4j.model.ChatHistory;

import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitResult;

public class PostStreamChatRepository {

    Logger logger = Logger.getLogger(PostStreamChatRepository.class.getName());

    private Properties properties;
    private OpenAIAsyncClient openAIAsyncClient;
    private CountDownLatch latch;

    String answer = "";

    public PostStreamChatRepository() {
        this.properties = new Properties();
        this.latch = new CountDownLatch(1);
        try {
            this.properties.load(this.getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
        this.openAIAsyncClient = new OpenAIClientBuilder()
                .credential(
                        new AzureKeyCredential(
                                properties.getProperty("aoai.apikey")))
                .endpoint(properties.getProperty("azure.openai.url"))
                .buildAsyncClient();
    }

    public String postChat(String message, List<ChatHistory> chatHistories, Sinks.Many<String> sink) {
        List<ChatMessage> chatMessages = createChatMessages(message, chatHistories);
        openAIAsyncClient.getChatCompletionsStream(properties.getProperty("azure.openai.model.name"),
                new ChatCompletionsOptions(chatMessages)).subscribe(chatCompletions -> {
                    chatCompletions.getChoices().stream()
                            .map(ChatChoice::getDelta)
                            .map(ChatMessage::getContent)
                            .filter(content -> content != null)
                            .forEach(content -> {
                                if (content.contains(" ")) {
                                    content = content.replace(" ", "<SPECIAL_WHITE_SPACE>");
                                }
                                // Server Sent Event で送信
                                EmitResult emitResult = sink.tryEmitNext(content);
                                showDetailErrorReasonForSSE(emitResult, content, message);
                                answer += content;
                                try {
                                    TimeUnit.MILLISECONDS.sleep(20);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                }, error -> {
                    logger.warning(error.getMessage());
                    latch.countDown();
                }, () -> {
                    latch.countDown();
                });
        try {
            latch.await();
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return answer;
    }

    private List<ChatMessage> createChatMessages(String userInput, List<ChatHistory> chatHistories) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(ChatRole.SYSTEM).setContent(properties.getProperty("system.defenition")));
        if (chatHistories.size() > 0) {
            chatHistories.forEach(chatHistory -> {
                if (chatHistory.getRole() == ChatRole.ASSISTANT) {
                    chatMessages.add(new ChatMessage(ChatRole.ASSISTANT).setContent(chatHistory.getAnswer()));
                } else {
                    chatMessages.add(new ChatMessage(ChatRole.USER).setContent(chatHistory.getQuestion()));
                }
            });
        }
        chatMessages.add(new ChatMessage(ChatRole.USER).setContent(userInput));
        return chatMessages;
    }

    private void showDetailErrorReasonForSSE(EmitResult result, String returnValue, String data) {
        if (result.isFailure()) {
            logger.warning(data);
            if (result == EmitResult.FAIL_OVERFLOW) {
                logger.warning("Overflow: " + returnValue + " " + data);
            } else if (result == EmitResult.FAIL_NON_SERIALIZED) {
                logger.warning("Non-serialized: " + returnValue + " " + data);
            } else if (result == EmitResult.FAIL_ZERO_SUBSCRIBER) {
                logger.warning("Zero subscriber: " + returnValue + " " + data);
            } else if (result == EmitResult.FAIL_TERMINATED) {
                logger.warning("Terminated: " + returnValue + " " + data);
            } else if (result == EmitResult.FAIL_CANCELLED) {
                logger.warning("Cancelled: " + returnValue + " " + data);
            }
        }
    }
}
