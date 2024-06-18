package com.example.creditunion.account.exception;

import static com.example.creditunion.account.common.Constants.BASE_URL_V1;
import com.example.creditunion.account.controller.AccountController;
import com.example.creditunion.account.exception.GlobalExceptionHandler;
import com.example.creditunion.account.exception.InternalServerException;
import com.example.creditunion.account.model.Account;
import com.example.creditunion.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import java.util.Optional;

@WebMvcTest(AccountController.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(accountController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testGetAccountById_ResourceNotFoundException() throws Exception {

        Long accountId = 10L;
        when(accountService.getAccountById(accountId)).thenReturn(Optional.empty());

        MvcResult result = mockMvc.perform(get(BASE_URL_V1 + "/accounts/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String message = result.getResponse().getContentAsString();
        assertEquals("Account not found with id: 10", message);
    }

    @Test
    public void testUpdateAccount_InternalServerException() throws Exception {

        Long accountId = 20L;
        Account updatedAccount = new Account();
        updatedAccount.setAddress("1234 Main St");

        when(accountService.updateAccount(anyLong(), any(Account.class)))
                .thenThrow(new InternalServerException("Internal server error"));

        MvcResult result = mockMvc.perform(put(BASE_URL_V1 + "/accounts/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAccount)))
                .andExpect(status().isInternalServerError())
                .andReturn();

        String message = result.getResponse().getContentAsString();
        assertEquals("Internal server error", message);
    }

    @Test
    public void testCreateAccount_MethodArgumentNotValidException() throws Exception {

        String invalidAccountJson = "{\\";

        mockMvc.perform(post(BASE_URL_V1 + "/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidAccountJson))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}