package com.koheisaito.sk4j.service;

import java.util.UUID;

import com.azure.ai.openai.models.ChatRole;
import com.koheisaito.sk4j.model.ChatHistory;
import com.koheisaito.sk4j.repository.CreateChatHistoryRepository;

public class CreateChatHistoryService {

    private CreateChatHistoryRepository createChatHistoryRepository;

    public CreateChatHistoryService(CreateChatHistoryRepository createChatHistoryRepository) {
        this.createChatHistoryRepository = createChatHistoryRepository;
    }

    public ChatHistory createChatHistory(UUID userId, String userInput, String answer) {
        ChatHistory chatHistory = buildChatHistory(userId.toString(), userInput, answer);
        return createChatHistoryRepository.createChatHistory(chatHistory);
    }

    // FIXME: userInput と answer で分ける
    // FIXME: Context を作る時は、timestamp 順に並べる
    // FIXME: Cosmos DB SDK ver 4.47.0 だと動かない
    private ChatHistory buildChatHistory(String userId, String userInput, String answer) {
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setId(UUID.randomUUID().toString());
        chatHistory.setUserId(userId);
        chatHistory.setQuestion(userInput);
        chatHistory.setAnswer(answer);
        chatHistory.setRole(ChatRole.ASSISTANT);
        chatHistory.setTimestamp((int) (System.currentTimeMillis() / 1000));
        return chatHistory;
    }
}
