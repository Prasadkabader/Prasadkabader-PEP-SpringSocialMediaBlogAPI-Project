package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountService accountService;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountService accountService) {
        this.messageRepository = messageRepository;
        this.accountService = accountService;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    public List<Message> getMessagesByUser(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }

    public Message createMessage(Message message) {
        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Message text must be between 1 and 255 characters");
        }
        
        if (accountService.getAccountById(message.getPostedBy()).isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        
        return messageRepository.save(message);
    }

    public boolean updateMessage(Integer messageId, String messageText) {
        if (messageText.isBlank() || messageText.length() > 255) {
            return false;
        }
        
        return messageRepository.findById(messageId)
            .map(message -> {
                message.setMessageText(messageText);
                messageRepository.save(message);
                return true;
            })
            .orElse(false);
    }

    public boolean deleteMessage(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return true;
        }
        return false;
    }
}
