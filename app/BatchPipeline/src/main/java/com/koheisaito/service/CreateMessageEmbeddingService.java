package com.koheisaito.service;

import java.util.List;

import com.koheisaito.repository.CreateMessageEmbeddingRepository;

public class CreateMessageEmbeddingService {

    private CreateMessageEmbeddingRepository createMessageEmbeddingRepository;

    public CreateMessageEmbeddingService(CreateMessageEmbeddingRepository createMessageEmbeddingRepository) {
        this.createMessageEmbeddingRepository = createMessageEmbeddingRepository;
    }

    public List<String> createMessageEmbedding(List<String> keys) {
        return createMessageEmbeddingRepository.createMessageEmbedding(keys);
    }
}
