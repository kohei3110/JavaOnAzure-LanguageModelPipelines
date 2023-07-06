package com.koheisaito.sk4j.service;

import java.util.List;

import com.koheisaito.sk4j.repository.PostChatRepository;

public class PostChatService {

    private PostChatRepository postChatRepository;

    public PostChatService(PostChatRepository postChatRepository) {
        this.postChatRepository = postChatRepository;
    }

    public List<String> postChat(String message) {
        return postChatRepository.postChat(message);
    }
}
