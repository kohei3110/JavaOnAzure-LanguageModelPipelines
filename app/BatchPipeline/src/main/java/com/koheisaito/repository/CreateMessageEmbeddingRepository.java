package com.koheisaito.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.Embeddings;
import com.azure.ai.openai.models.EmbeddingsOptions;
import com.azure.core.credential.AzureKeyCredential;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.Jedis;

public class CreateMessageEmbeddingRepository {
    Logger logger = Logger.getLogger(CreateMessageEmbeddingRepository.class.getName());

    private Properties properties;
    private Jedis jedis;
    private OpenAIClient openAIClient;

    public CreateMessageEmbeddingRepository() {
        this.properties = new Properties();
        try {
            this.properties.load(this.getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
        this.jedis = new redis.clients.jedis.Jedis(properties.getProperty("redis.hostname"), 6380,
                DefaultJedisClientConfig.builder()
                        .password(properties.getProperty("redis.key")).ssl(true).build());
        this.openAIClient = new OpenAIClientBuilder()
                .credential(
                        new AzureKeyCredential(
                                properties.getProperty("aoai.apikey")))
                .endpoint(properties.getProperty("azure.openai.url"))
                .buildClient();
    }

    public List<String> createMessageEmbedding(List<String> keys) {
        List<List<Double>> embedding = getEmbedding(keys);
        List<String> results = new ArrayList<String>();
        embedding.forEach(
                em -> {
                    String res = jedis.set(em.toString(), em.toString());
                    results.add(res);
                });
        return results;
    }

    private List<List<Double>> getEmbedding(List<String> messages) {
        List<List<Double>> results = new ArrayList<List<Double>>();
        messages.forEach(
                message -> {
                    EmbeddingsOptions embeddingsOptions = new EmbeddingsOptions(Arrays.asList(message));
                    Embeddings embeddings = openAIClient.getEmbeddings(
                            properties.getProperty("azure.openai.model.name"),
                            embeddingsOptions);
                    results.add(embeddings.getData().stream().findFirst().get().getEmbedding());
                });
        return results;
    }
}
