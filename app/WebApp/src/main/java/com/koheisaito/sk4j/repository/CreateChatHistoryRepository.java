package com.koheisaito.sk4j.repository;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosItemResponse;
import com.koheisaito.sk4j.model.ChatHistory;

public class CreateChatHistoryRepository {
    Logger logger = Logger.getLogger(PostChatRepository.class.getName());

    private Properties properties;
    private CosmosClient cosmosClient;
    private CosmosDatabase cosmosDatabase;
    private CosmosContainer cosmosContainer;

    public CreateChatHistoryRepository() {
        this.properties = new Properties();
        try {
            this.properties.load(this.getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
        this.cosmosClient = new CosmosClientBuilder()
                .endpoint(properties.getProperty("cosmos.endpoint"))
                .key(properties.getProperty("cosmos.key"))
                .contentResponseOnWriteEnabled(true)
                .buildClient();
        this.cosmosDatabase = cosmosClient.getDatabase(properties.getProperty("cosmos.database"));
        this.cosmosContainer = cosmosDatabase.getContainer(properties.getProperty("cosmos.container"));
    }

    public ChatHistory createChatHistory(ChatHistory chatHistory) {
        CosmosItemResponse<ChatHistory> response = cosmosContainer.createItem(chatHistory);
        return response.getItem();
    }
}