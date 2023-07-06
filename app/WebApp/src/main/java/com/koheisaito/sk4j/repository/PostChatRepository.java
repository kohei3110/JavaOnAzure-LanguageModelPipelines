package com.koheisaito.sk4j.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatMessage;
import com.azure.ai.openai.models.ChatRole;
import com.azure.core.credential.AzureKeyCredential;

public class PostChatRepository {

    Logger logger = Logger.getLogger(PostChatRepository.class.getName());

    private Properties properties;
    private OpenAIClient openAIClient;

    public PostChatRepository() {
        this.properties = new Properties();
        try {
            this.properties.load(this.getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
        this.openAIClient = new OpenAIClientBuilder()
                .credential(
                        new AzureKeyCredential(
                                properties.getProperty("aoai.apikey")))
                .endpoint(properties.getProperty("azure.openai.url"))
                .buildClient();
    }

    public List<String> postChat(String message) {
        List<String> chatResult = new ArrayList<String>();
        ChatCompletions chatCompletions = openAIClient.getChatCompletions(
                properties.getProperty("azure.openai.model.name"),
                new ChatCompletionsOptions(createChatMessages(message)));
        chatCompletions.getChoices().forEach(choice -> {
            ChatMessage chatMessage = choice.getMessage();
            String content = chatMessage.getContent();
            chatResult.add(content);
        });
        return chatResult;
    }

    private List<ChatMessage> createChatMessages(String userInput) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(ChatRole.SYSTEM).setContent(properties.getProperty("system.defenition")));
        chatMessages.add(new ChatMessage(ChatRole.USER).setContent(userInput));
        return chatMessages;
    }
}
