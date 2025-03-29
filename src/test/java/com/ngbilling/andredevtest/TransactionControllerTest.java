package com.ngbilling.andredevtest;

import com.ngbilling.andredevtest.controller.TransactionController;
import com.ngbilling.andredevtest.controller.dto.AccountDTO;
import com.ngbilling.andredevtest.controller.dto.TransactionDTO;
import com.ngbilling.andredevtest.model.Account;
import com.ngbilling.andredevtest.service.AccountService;
import com.ngbilling.andredevtest.service.BalanceCalculator;
import com.ngbilling.andredevtest.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService service;

    @Mock
    private AccountService accountService;

    @Mock
    private BalanceCalculator calculator;

    @InjectMocks
    private TransactionController controller;

    private TransactionDTO transactionDTO;
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(123456, new BigDecimal("100.00"));
    }

    @Test
    void createTransaction_success() {
        transactionDTO = new TransactionDTO("D", 123456, new BigDecimal("10"));
        when(accountService.findByNumber(transactionDTO.number())).thenReturn(Optional.of(account));
        BigDecimal newBalance = calculator.calculateFinalBalance(transactionDTO.paymentMethod(), account.getBalance(), transactionDTO.value());

        ResponseEntity<?> response = controller.createTransaction(transactionDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(new AccountDTO(account.getNumber(), newBalance), response.getBody());
        assertEquals(newBalance, account.getBalance());
        verify(service).save(transactionDTO, account);
    }

    @Test
    void createTransaction_accountNotFound() {
        transactionDTO = new TransactionDTO("D", 123456, new BigDecimal("100.00"));
        when(accountService.findByNumber(transactionDTO.number())).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.createTransaction(transactionDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Map.of("mensagem", "Não existe uma conta com o número informado!"), response.getBody());
    }

    @Test
    void createTransaction_insufficientBalance() {
        account.setBalance(new BigDecimal("50"));
        transactionDTO = new TransactionDTO("D", 123456, new BigDecimal("100.00"));
        when(accountService.findByNumber(transactionDTO.number())).thenReturn(Optional.of(account));

        ResponseEntity<?> response = controller.createTransaction(transactionDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Map.of("mensagem", "Saldo insuficiente!"), response.getBody());
    }

    @Test
    void createTransaction_exceptionThrown() {
        transactionDTO = new TransactionDTO("D", 123456, new BigDecimal("100.00"));
        when(accountService.findByNumber(transactionDTO.number())).thenReturn(Optional.of(account));
        when(calculator.calculateFinalBalance(transactionDTO.paymentMethod(), account.getBalance(), transactionDTO.value()))
                .thenThrow(new RuntimeException("Test exception"));

        ResponseEntity<?> response = controller.createTransaction(transactionDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Map.of("mensagem", "Erro ao processar transação."), response.getBody());
        verify(service, never()).save(transactionDTO, account);
    }
}