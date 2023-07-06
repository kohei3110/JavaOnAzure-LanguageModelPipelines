package com.koheisaito.sk4j;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.koheisaito.sk4j.model.ChatHistory;
import com.koheisaito.sk4j.model.ChatRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@SpringBootApplication
@RestController
@ComponentScan("{com.kohei3110.sk4j}")
public class ServerController {

	Factory factory = new Factory();

	public static void main(String[] args) {
		SpringApplication.run(ServerController.class, args);
	}

	@PostMapping("/chats")
	public List<String> postChat(@RequestBody ChatRequest request) {
		return factory.injectPostChatService().postChat(request.getMessage());
	}

	@PostMapping("/streamChats")
	public void postStreamChat(@RequestBody String userInput, @RequestParam UUID userId) {
		Sinks.Many<String> sink = factory.injectGetUserSyncService().getUserSink(userId);
		// DB から ChatHistory を取得
		List<ChatHistory> chatHistories = factory.injectGetChatHistoryService().getChatHistory(userId);
		String answer = factory.injectPostStreamChatService().postStreamChat(userInput, chatHistories, sink);
		// DB 保存
		factory.injectCreateChatHistoryService().createChatHistory(userId, userInput, answer);
	}

	@GetMapping(path = "/sseStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@ResponseBody
	public Flux<String> sseStream(@RequestParam("userId") UUID userId) {
		Sinks.Many<String> sink = factory.injectGetUserSyncService().getUserSink(userId);
		if (sink == null) {
			sink = factory.injectGetUserSyncService().createUserSink(userId);
		}
		return sink.asFlux().delayElements(Duration.ofMillis(10));
	}
}
