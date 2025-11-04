package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account register(Account account) {
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        
        if (accountRepository.existsByUsername(account.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        return accountRepository.save(account);
    }

    public Optional<Account> login(String username, String password) {
        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            return Optional.empty();
        }
        
        return accountRepository.findByUsernameAndPassword(username, password);
    }

    public Optional<Account> getAccountById(Integer accountId) {
        return accountRepository.findById(accountId);
    }
}
