package com.ngbilling.andredevtest.controller;

import com.ngbilling.andredevtest.controller.dto.AccountDTO;
import com.ngbilling.andredevtest.model.Account;
import com.ngbilling.andredevtest.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/conta")
public class AccountController {

    @Autowired
    private AccountService service;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody AccountDTO dto) {
        Optional<Account> accountToValidate = service.findByNumber(dto.number());

        if (accountToValidate.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Conta com este número já existe.");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        AccountDTO newAccount = null;
        try {
            newAccount = service.save(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
    }

    @GetMapping
    public ResponseEntity<AccountDTO> getAccountByNumber(@RequestParam(name = "numero_conta") int number) {
        return service
                .findByNumber(number)
                .map(acc -> {
                    AccountDTO accountDTO = new AccountDTO(
                            acc.getNumber(),
                            acc.getBalance()
                    );
                    return ResponseEntity.ok(accountDTO);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
