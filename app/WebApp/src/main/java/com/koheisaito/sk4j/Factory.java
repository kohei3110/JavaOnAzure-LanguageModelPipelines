package com.koheisaito.sk4j;

import com.koheisaito.sk4j.repository.CreateChatHistoryRepository;
import com.koheisaito.sk4j.repository.GetChatHistoryRepository;
import com.koheisaito.sk4j.repository.GetMessageEmbeddingRepository;
import com.koheisaito.sk4j.repository.PostChatRepository;
import com.koheisaito.sk4j.repository.PostStreamChatRepository;
import com.koheisaito.sk4j.service.CreateChatHistoryService;
import com.koheisaito.sk4j.service.GetChatHistoryService;
import com.koheisaito.sk4j.service.GetMessageEmbeddingService;
import com.koheisaito.sk4j.service.GetUserSyncService;
import com.koheisaito.sk4j.service.PostChatService;
import com.koheisaito.sk4j.service.PostStreamChatService;

public class Factory {

    private PostChatRepository postChatRepository;
    private PostChatService postChatService;
    private PostStreamChatRepository postStreamChatRepository;
    private PostStreamChatService postStreamChatService;
    private GetUserSyncService getUserSyncService;
    private CreateChatHistoryRepository createChatHistoryRepository;
    private CreateChatHistoryService createChatHistoryService;
    private GetChatHistoryRepository getChatHistoryRepository;
    private GetChatHistoryService getChatHistoryService;
    private GetMessageEmbeddingRepository getMessageEmbeddingRepository;
    private GetMessageEmbeddingService getMessageEmbeddingService;

    public Factory() {
        this.postChatRepository = new PostChatRepository();
        this.postChatService = new PostChatService(this.postChatRepository);
        this.postStreamChatRepository = new PostStreamChatRepository();
        this.postStreamChatService = new PostStreamChatService(this.postStreamChatRepository);
        this.getUserSyncService = new GetUserSyncService();
        this.createChatHistoryRepository = new CreateChatHistoryRepository();
        this.createChatHistoryService = new CreateChatHistoryService(this.createChatHistoryRepository);
        this.getChatHistoryRepository = new GetChatHistoryRepository();
        this.getChatHistoryService = new GetChatHistoryService(this.getChatHistoryRepository);
        this.getMessageEmbeddingRepository = new GetMessageEmbeddingRepository();
        this.getMessageEmbeddingService = new GetMessageEmbeddingService(this.getMessageEmbeddingRepository);
    }

    public PostChatService injectPostChatService() {
        return this.postChatService;
    }

    public PostStreamChatService injectPostStreamChatService() {
        return this.postStreamChatService;
    }

    public GetUserSyncService injectGetUserSyncService() {
        return this.getUserSyncService;
    }

    public CreateChatHistoryService injectCreateChatHistoryService() {
        return this.createChatHistoryService;
    }

    public GetChatHistoryService injectGetChatHistoryService() {
        return this.getChatHistoryService;
    }

    public GetMessageEmbeddingService injectGetMessageEmbeddingService() {
        return this.getMessageEmbeddingService;
    }
}
