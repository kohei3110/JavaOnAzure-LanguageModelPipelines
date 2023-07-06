package com.koheisaito.sk4j.service;

import com.koheisaito.sk4j.repository.GetMessageEmbeddingRepository;

public class GetMessageEmbeddingService {

    private GetMessageEmbeddingRepository getMessageEmbeddingRepository;

    public GetMessageEmbeddingService(GetMessageEmbeddingRepository getMessageEmbeddingRepository) {
        this.getMessageEmbeddingRepository = getMessageEmbeddingRepository;
    }

    public String getMessageEmbedding(String message) {
        return getMessageEmbeddingRepository.getMessageEmbedding(message);
    }
}
