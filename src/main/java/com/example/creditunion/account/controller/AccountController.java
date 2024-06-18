package com.example.creditunion.account.controller;

import com.example.creditunion.account.exception.ResourceNotFoundException;
import com.example.creditunion.account.model.Account;
import com.example.creditunion.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@Validated
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable @NotNull Long id) {
        return accountService.getAccountById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable @NotNull Long id,
                                                 @Valid @RequestBody Account accountDetails) {
        return ResponseEntity.ok(accountService.updateAccount(id, accountDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable @NotNull Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAccounts(@RequestParam(required = false) Long id,
                                            @RequestParam(required = false) String name) {
        if (id != null) {
            return accountService.getAccountById(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        } else if (name != null && !name.trim().isEmpty()) {
            List<Account> accounts = accountService.searchAccountsByName(name);
            if (accounts.isEmpty()) {
                throw new ResourceNotFoundException("No accounts found with name: " + name);
            }
            return ResponseEntity.ok(accounts);
        } else {
            return ResponseEntity.badRequest().body("Please provide a name or id to search.");
        }
    }

}
