package com.koheisaito.sk4j.repository;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.Jedis;

public class GetMessageEmbeddingRepository {
    Logger logger = Logger.getLogger(GetMessageEmbeddingRepository.class.getName());

    private Properties properties;
    private Jedis jedis;

    public GetMessageEmbeddingRepository() {
        this.properties = new Properties();
        try {
            this.properties.load(this.getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
        this.jedis = new redis.clients.jedis.Jedis(properties.getProperty("redis.hostname"), 6380,
                DefaultJedisClientConfig.builder()
                        .password(properties.getProperty("redis.key")).ssl(true).build());
    }

    public String getMessageEmbedding(String key) {
        return jedis.get(key);
    }
}
