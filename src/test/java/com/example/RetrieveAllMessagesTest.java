package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.example.entity.Message;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RetrieveAllMessagesTest {
	ApplicationContext app;
    HttpClient webClient;
    ObjectMapper objectMapper;

    /**
     * Before every test, reset the database, restart the Javalin app, and create a new webClient and ObjectMapper
     * for interacting locally on the web.
     * @throws InterruptedException
     */
    @BeforeEach
    public void setUp() throws InterruptedException {
        webClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
        String[] args = new String[] {};
        app = SpringApplication.run(SocialMediaApp.class, args);
        Thread.sleep(500);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
    	Thread.sleep(500);
    	SpringApplication.exit(app);
    }
    
    @Test
    public void getAllMessagesMessagesAvailable() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages"))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status, "Expected Status Code 200 - Actual Code was: " + status);
        List<Message> actualResult = objectMapper.readValue(response.body().toString(), new TypeReference<List<Message>>(){});
        
        // Verify we got 3 messages
        Assertions.assertEquals(3, actualResult.size(), "Expected 3 messages but got " + actualResult.size());
        
        // Verify all expected messages are present regardless of order
        List<Message> expectedMessages = List.of(
            new Message(9996, 9996, "test message 3", 1669947792L),
            new Message(9997, 9997, "test message 2", 1669947792L),
            new Message(9999, 9999, "test message 1", 1669947792L)
        );
        
        Assertions.assertTrue(actualResult.containsAll(expectedMessages), 
            "Expected all test messages to be present in the response");
        
        // Verify the messages are in descending order by messageId
        for (int i = 0; i < actualResult.size() - 1; i++) {
            Assertions.assertTrue(actualResult.get(i).getMessageId() > actualResult.get(i + 1).getMessageId(),
                "Messages should be in descending order by messageId");
        }
    }
}
