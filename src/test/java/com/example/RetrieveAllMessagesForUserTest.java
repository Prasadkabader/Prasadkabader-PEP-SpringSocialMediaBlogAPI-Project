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

public class RetrieveAllMessagesForUserTest {
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
    
    /**
     * Sending an http request to GET localhost:8080/accounts/9999/messages (messages exist for user) 
     * 
     * Expected Response:
     *  Status Code: 200
     *  Response Body: JSON representation of a list of messages
     */
    @Test
    public void getAllMessagesFromUserMessageExists() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/accounts/9999/messages"))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status, "Expected Status Code 200 - Actual Code was: " + status);
        
        // Parse the response body
        List<Message> actualResult = objectMapper.readValue(response.body(), new TypeReference<List<Message>>(){});
        
        // Verify we got at least one message
        Assertions.assertFalse(actualResult.isEmpty(), "Expected at least one message for user 9999");
        
        // Verify the message content
        Message expectedMessage = new Message(9999, 9999, "test message 1", 1669947792L);
        Assertions.assertTrue(actualResult.contains(expectedMessage), 
            "Expected to find message with content 'test message 1' for user 9999");
    }
    
    /**
     * Sending an http request to GET localhost:8080/accounts/9998/messages (messages does NOT exist for user) 
     * 
     * Expected Response:
     *  Status Code: 200
     *  Response Body: Empty array []
     */
    @Test
    public void getAllMessagesFromUserNoMessagesFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/accounts/9998/messages"))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        
        // Verify status code
        Assertions.assertEquals(200, status, "Expected Status Code 200 - Actual Code was: " + status);
        
        // Parse the response body (should be an empty array)
        List<Message> actualResult = objectMapper.readValue(
            response.body().isEmpty() ? "[]" : response.body(), 
            new TypeReference<List<Message>>(){});
            
        // Verify the result is an empty list
        Assertions.assertTrue(actualResult.isEmpty(), "Expected empty list but got: " + actualResult);
    }
}
