package com.koheisaito.sk4j.service;

import java.util.List;

import com.koheisaito.sk4j.model.ChatHistory;
import com.koheisaito.sk4j.repository.PostStreamChatRepository;

import reactor.core.publisher.Sinks;

public class PostStreamChatService {

    private PostStreamChatRepository postStreamChatRepository;

    public PostStreamChatService(PostStreamChatRepository postStreamChatRepository) {
        this.postStreamChatRepository = postStreamChatRepository;
    }

    public String postStreamChat(String message, List<ChatHistory> chatHistories, Sinks.Many<String> sink) {
        return postStreamChatRepository.postChat(message, chatHistories, sink);
    }
}
