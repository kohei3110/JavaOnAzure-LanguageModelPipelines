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

    public ChatHistory createChatHistoryWithRole(UUID userId, String content, ChatRole role) {
        ChatHistory chatHistory = buildChatHistoryWithRole(userId.toString(), content, role);
        return createChatHistoryRepository.createChatHistory(chatHistory);
    }

    // FIXME: Cosmos DB SDK ver 4.47.0 だと動かない
    private ChatHistory buildChatHistoryWithRole(String userId, String content, ChatRole role) {
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setId(UUID.randomUUID().toString());
        chatHistory.setUserId(userId);
        chatHistory.setContent(content);
        chatHistory.setRole(role);
        chatHistory.setTimestamp((int) (System.currentTimeMillis() / 1000));
        return chatHistory;
    }
}
