package com.example.controller;

import com.example.entity.Message;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountMessageController {
    private final MessageService messageService;

    @Autowired
    public AccountMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Get all messages for a specific user
     * @param accountId The ID of the account to get messages for
     * @return List of messages for the specified user
     */
    @GetMapping("/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByUser(accountId);
        return ResponseEntity.ok(messages);
    }
}
