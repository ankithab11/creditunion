package com.example.creditunion.account.service;

import com.example.creditunion.account.model.Account;
import com.example.creditunion.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAccounts() {
        Account account1 = new Account();
        account1.setFirstName("John");
        account1.setLastName("Doe");
        account1.setAddress("123 Main St");

        Account account2 = new Account();
        account2.setFirstName("Jane");
        account2.setLastName("Smith");
        account2.setAddress("456 Elm St");

        List<Account> accounts = Arrays.asList(account1, account2);
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.getAllAccounts();
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    @Test
    public void testGetAccountById() {
        Account account = new Account();
        account.setFirstName("John");
        account.setLastName("Doe");
        account.setAddress("123 Main St");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Optional<Account> result = accountService.getAccountById(1L);
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    public void testCreateAccount() {
        Account account = new Account();
        account.setFirstName("John");
        account.setLastName("Doe");
        account.setAddress("123 Main St");
        when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.createAccount(account);
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    public void testUpdateAccount() {
        Account account = new Account();
        account.setFirstName("John");
        account.setLastName("Doe");
        account.setAddress("123 Main St");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account updatedAccountDetails = new Account();
        updatedAccountDetails.setFirstName("Jane");
        updatedAccountDetails.setLastName("Smith");
        updatedAccountDetails.setAddress("456 Elm St");

        when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.updateAccount(1L, updatedAccountDetails);
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
    }

    @Test
    public void testUpdateAccount_AccountNotFound() {
        // Arrange
        Long nonExistentAccountId = 999L;
        Account updatedAccountDetails = new Account();
        updatedAccountDetails.setFirstName("Jane");
        updatedAccountDetails.setLastName("Smith");
        updatedAccountDetails.setAddress("456 Elm St");

        when(accountRepository.findById(nonExistentAccountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            accountService.updateAccount(nonExistentAccountId, updatedAccountDetails);
        });

        verify(accountRepository, times(1)).findById(nonExistentAccountId);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void testDeleteAccount() {
        Account account = new Account();
        account.setFirstName("John");
        account.setLastName("Doe");
        account.setAddress("123 Main St");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        doNothing().when(accountRepository).delete(account);

        accountService.deleteAccount(1L);
        verify(accountRepository, times(1)).delete(account);
    }

    @Test
    public void testDeleteAccount_AccountNotFound() {
        // Arrange
        Long nonExistentAccountId = 999L;
        when(accountRepository.findById(nonExistentAccountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            accountService.deleteAccount(nonExistentAccountId);
        });

        verify(accountRepository, times(1)).findById(nonExistentAccountId);
        verify(accountRepository, never()).delete(any(Account.class));
    }
}
