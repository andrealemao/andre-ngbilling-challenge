package com.ngbilling.andredevtest.controller;

import com.ngbilling.andredevtest.controller.dto.AccountDTO;
import com.ngbilling.andredevtest.controller.dto.TransactionDTO;
import com.ngbilling.andredevtest.model.Account;
import com.ngbilling.andredevtest.service.AccountService;
import com.ngbilling.andredevtest.service.BalanceCalculator;
import com.ngbilling.andredevtest.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/transacao")
public class TransactionController {

    private final TransactionService service;
    private final AccountService accountService;
    private final BalanceCalculator calculator;

    @Autowired
    public TransactionController(TransactionService service, AccountService accountService, BalanceCalculator calculator) {
        this.service = service;
        this.accountService = accountService;
        this.calculator = calculator;
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO dto) {
        Optional<Account> accountOpt = accountService.findByNumber(dto.number());

        if (accountOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensagem", "Não existe uma conta com o número informado!"));
        }

        Account account = accountOpt.get();

        if (dto.value().compareTo(account.getBalance()) > 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensagem", "Saldo insuficiente!"));

        try {
            BigDecimal newBalance = calculator.calculateFinalBalance(dto.paymentMethod(), account.getBalance(), dto.value());
            account.setBalance(newBalance);
            service.save(dto, account);

            AccountDTO accountDTO = new AccountDTO(account.getNumber(), account.getBalance());
            return ResponseEntity.status(HttpStatus.CREATED).body(accountDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("mensagem", "Erro ao processar transação."));
        }

    }
}
