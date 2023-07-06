package com.koheisaito.sk4j.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import reactor.core.publisher.Sinks;

public class GetUserSyncService {

    private Map<UUID, Sinks.Many<String>> userSinks;

    public GetUserSyncService() {
        this.userSinks = new ConcurrentHashMap<>();
    }

    public Sinks.Many<String> getUserSink(UUID userId) {
        return userSinks.get(userId);
    }

    public Sinks.Many<String> createUserSink(UUID userId) {
        Sinks.Many<String> sink = Sinks.many().multicast().directBestEffort();
        userSinks.put(userId, sink);
        return sink;
    }
}
