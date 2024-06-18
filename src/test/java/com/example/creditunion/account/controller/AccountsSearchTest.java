package com.example.creditunion.account.controller;

import com.example.creditunion.account.exception.ResourceNotFoundException;
import com.example.creditunion.account.model.Account;
import com.example.creditunion.account.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountsSearchTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSearchAccountsById_Found() {
        Account account = new Account();
        account.setId(1L);
        account.setFirstName("John");
        account.setLastName("Doe");
        account.setAddress("123 Main St");

        when(accountService.getAccountById(1L)).thenReturn(Optional.of(account));

        ResponseEntity<?> response = accountController.searchAccounts(1L, null);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(account, response.getBody());

        verify(accountService, times(1)).getAccountById(1L);
        verifyNoMoreInteractions(accountService);
    }

    @Test
    public void testSearchAccountsById_NotFound() {
        when(accountService.getAccountById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountController.searchAccounts(1L, null);
        });

        assertEquals("Account not found with id: 1", exception.getMessage());

        verify(accountService, times(1)).getAccountById(1L);
        verifyNoMoreInteractions(accountService);
    }

    @Test
    public void testSearchAccountsByName_Found() {
        Account account1 = new Account();
        account1.setFirstName("John");
        account1.setLastName("Doe");
        account1.setAddress("123 Main St");

        Account account2 = new Account();
        account2.setFirstName("John");
        account2.setLastName("Smith");
        account2.setAddress("456 Elm St");

        List<Account> accounts = Arrays.asList(account1, account2);
        when(accountService.searchAccountsByName("John")).thenReturn(accounts);

        ResponseEntity<?> response = accountController.searchAccounts(null, "John");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(accounts, response.getBody());

        verify(accountService, times(1)).searchAccountsByName("John");
        verifyNoMoreInteractions(accountService);
    }

    @Test
    public void testSearchAccountsByName_NotFound() {
        when(accountService.searchAccountsByName("NonExistentName")).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountController.searchAccounts(null, "NonExistentName");
        });

        assertEquals("No accounts found with name: NonExistentName", exception.getMessage());

        verify(accountService, times(1)).searchAccountsByName("NonExistentName");
        verifyNoMoreInteractions(accountService);
    }

    @Test
    public void testSearchAccountsWithoutParams() {
        ResponseEntity<?> response = accountController.searchAccounts(null, null);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Please provide a name or id to search.", response.getBody());

        verifyNoInteractions(accountService);
    }

    @Test
    public void testSearchAccountsWithBothIdAndName_IdTakesPrecedence() {
        Account account = new Account();
        account.setId(1L);
        account.setFirstName("John");
        account.setLastName("Doe");
        account.setAddress("123 Main St");

        when(accountService.getAccountById(1L)).thenReturn(Optional.of(account));

        ResponseEntity<?> response = accountController.searchAccounts(1L, "John");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(account, response.getBody());

        verify(accountService, times(1)).getAccountById(1L);
        verifyNoMoreInteractions(accountService);
    }
}

