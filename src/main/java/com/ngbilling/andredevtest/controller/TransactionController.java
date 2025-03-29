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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/transacao")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BalanceCalculator calculator;

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO dto) {
        Optional<Account> account = accountService.findByNumber(dto.number());

        if (account.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Não existe uma conta com o número informado!");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        BigDecimal balance = account.get().getBalance();
        if (dto.value().compareTo(balance) > 0)
            return ResponseEntity.notFound().build();

        BigDecimal newBalance = calculator.calculateFinalBalance(dto.paymentMethod(), balance, dto.value());

        account.get().setBalance(newBalance);

        AccountDTO accountDTO = new AccountDTO(
                account.get().getNumber(),
                account.get().getBalance()
        );

        try {
            service.save(dto, account.get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(accountDTO);
    }
}
