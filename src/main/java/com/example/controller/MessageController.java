package com.example.controller;

import com.example.entity.Message;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        return messageService.getMessageById(messageId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok().build());
    }

    @GetMapping("/accounts/{accountId}")
    public List<Message> getMessagesByUser(@PathVariable Integer accountId) {
        return messageService.getMessagesByUser(accountId);
    }

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        try {
            Message createdMessage = messageService.createMessage(message);
            return ResponseEntity.ok(createdMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<Integer> updateMessage(
            @PathVariable Integer messageId,
            @RequestBody Message messageUpdate) {
        
        boolean updated = messageService.updateMessage(
            messageId, 
            messageUpdate.getMessageText()
        );
        
        return updated ? 
            ResponseEntity.ok(1) : 
            ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Integer messageId) {
        boolean deleted = messageService.deleteMessage(messageId);
        return deleted ? 
            ResponseEntity.ok().build() : 
            ResponseEntity.ok().build();
    }
}
