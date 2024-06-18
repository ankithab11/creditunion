package com.example.creditunion.account.controller;

import com.example.creditunion.account.model.Account;
import com.example.creditunion.account.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.example.creditunion.account.common.Constants.BASE_URL_V1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        accountRepository.deleteAll();
    }

    @Test
    public void testCreateAccount() throws Exception {
        Account account = new Account();
        account.setFirstName("John");
        account.setLastName("Doe");
        account.setAddress("123 Main St");

        mockMvc.perform(post(BASE_URL_V1 + "/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.address").value("123 Main St"));
    }

    @Test
    public void testGetAllAccounts() throws Exception {
        Account account1 = new Account();
        account1.setFirstName("John");
        account1.setLastName("Doe");
        account1.setAddress("123 Main St");
        accountRepository.save(account1);

        Account account2 = new Account();
        account2.setFirstName("Jane");
        account2.setLastName("Smith");
        account2.setAddress("456 Elm St");
        accountRepository.save(account2);

        mockMvc.perform(get(BASE_URL_V1 + "/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].address").value("123 Main St"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].address").value("456 Elm St"));
    }

    @Test
    public void testGetAccountById() throws Exception {
        Account account = new Account();
        account.setFirstName("John");
        account.setLastName("Doe");
        account.setAddress("123 Main St");
        account = accountRepository.save(account);

        mockMvc.perform(get(BASE_URL_V1 + "/accounts/{id}", account.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(account.getId()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.address").value("123 Main St"));
    }

    @Test
    public void testUpdateAccount() throws Exception {
        Account account = new Account();
        account.setFirstName("John");
        account.setLastName("Doe");
        account.setAddress("123 Main St");
        account = accountRepository.save(account);

        Account updatedAccount = new Account();
        updatedAccount.setFirstName("Jane");
        updatedAccount.setLastName("Smith");
        updatedAccount.setAddress("456 Elm St");

        mockMvc.perform(put(BASE_URL_V1 + "/accounts/{id}", account.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAccount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(account.getId()))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.address").value("456 Elm St"));
    }

    @Test
    public void testDeleteAccount() throws Exception {
        Account account = new Account();
        account.setFirstName("John");
        account.setLastName("Doe");
        account.setAddress("123 Main St");
        account = accountRepository.save(account);

        mockMvc.perform(delete(BASE_URL_V1 + "/accounts/{id}", account.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BASE_URL_V1 + "/accounts/{id}", account.getId()))
                .andExpect(status().isNotFound());
    }

}
