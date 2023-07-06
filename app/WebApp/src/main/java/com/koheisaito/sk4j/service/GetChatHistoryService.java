package com.koheisaito.sk4j.service;

import java.util.List;
import java.util.UUID;

import com.koheisaito.sk4j.model.ChatHistory;
import com.koheisaito.sk4j.repository.GetChatHistoryRepository;

public class GetChatHistoryService {

    private GetChatHistoryRepository getChatHistoryRepository;

    public GetChatHistoryService(GetChatHistoryRepository getChatHistoryRepository) {
        this.getChatHistoryRepository = getChatHistoryRepository;
    }

    public List<ChatHistory> getChatHistory(UUID userId) {
        return getChatHistoryRepository.getChatHistoriesByUserId(userId.toString());
    }
}
