package com.ngbilling.andredevtest.controller;

import com.ngbilling.andredevtest.controller.dto.AccountDTO;
import com.ngbilling.andredevtest.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/conta")
public class AccountController {

    private final AccountService service;

    @Autowired
    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountDTO dto) {
        if (service.findByNumber(dto.number()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensagem", "Já existe uma conta com o número informado!"));
        }

        try {
            AccountDTO newAccount = service.save(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("mensagem", "Erro interno ao salvar a conta. Contate o suporte."));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAccountByNumber(@RequestParam(name = "numero_conta") int number) {
        return service.findByNumber(number)
                .map(account -> ResponseEntity.ok(new AccountDTO(account.getNumber(), account.getBalance())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.append(error.getDefaultMessage()).append("; ");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensagem", errorMessage.toString()));
    }
}
