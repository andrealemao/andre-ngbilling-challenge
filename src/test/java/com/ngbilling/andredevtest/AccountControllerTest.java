package com.ngbilling.andredevtest;

import com.ngbilling.andredevtest.controller.AccountController;
import com.ngbilling.andredevtest.controller.dto.AccountDTO;
import com.ngbilling.andredevtest.model.Account;
import com.ngbilling.andredevtest.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private Account existingAccount;
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        existingAccount = new Account(123456, new BigDecimal("100.00"));
        bindingResult = mock(BindingResult.class);
    }

    @Test
    void createAccount_success() {
        AccountDTO dto = new AccountDTO(123456, new BigDecimal("100.00"));
        when(accountService.findByNumber(dto.number())).thenReturn(Optional.empty());
        when(accountService.save(dto)).thenReturn(dto);

        ResponseEntity<?> response = accountController.createAccount(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(accountService).save(dto);
    }

    @Test
    void createAccount_conflict_accountAlreadyExists() {
        AccountDTO dto = new AccountDTO(123456, new BigDecimal("100.00"));

        when(accountService.findByNumber(dto.number())).thenReturn(Optional.of(existingAccount));

        ResponseEntity<?> response = accountController.createAccount(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(Map.of("mensagem", "Já existe uma conta com o número informado!"), response.getBody());
        verify(accountService, never()).save(dto);
    }
}
