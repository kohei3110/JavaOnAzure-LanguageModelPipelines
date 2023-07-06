package com.koheisaito.sk4j.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.FeedResponse;
import com.koheisaito.sk4j.model.ChatHistory;

public class GetChatHistoryRepository {

    Logger logger = Logger.getLogger(GetChatHistoryRepository.class.getName());

    private Properties properties;
    private CosmosClient cosmosClient;
    private CosmosDatabase cosmosDatabase;
    private CosmosContainer cosmosContainer;

    private static final String SQL_QUERY = "SELECT * FROM c WHERE c.userId = ";

    public GetChatHistoryRepository() {
        this.properties = new Properties();
        try {
            this.properties.load(this.getClass().getResourceAsStream("/application.properties"));
        } catch (Exception e) {
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

    public List<ChatHistory> getChatHistoriesByUserId(String userId) {
        Iterable<FeedResponse<ChatHistory>> response = cosmosContainer
                .queryItems(SQL_QUERY + "\"" + userId + "\"", new CosmosQueryRequestOptions(), ChatHistory.class)
                .iterableByPage();
        List<ChatHistory> chatHistories = new ArrayList<ChatHistory>();
        if (response == null) {
            return chatHistories;
        }
        for (FeedResponse<ChatHistory> page : response) {
            chatHistories.addAll(page.getResults());
        }
        return chatHistories;
    }
}
