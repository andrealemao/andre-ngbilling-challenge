package com.ngbilling.andredevtest.service.impl;

import com.ngbilling.andredevtest.controller.dto.AccountDTO;
import com.ngbilling.andredevtest.model.Account;
import com.ngbilling.andredevtest.repository.AccountRepository;
import com.ngbilling.andredevtest.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private final AccountRepository repository;

    public AccountServiceImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public AccountDTO save(AccountDTO dto) {
        Account account = new Account();
        account.setNumber(dto.number());
        account.setBalance(dto.balance());

        repository.save(account);

        return dto;
    }

    @Override
    public Optional<Account> findByNumber(int number) {
        return repository.findByNumber(number);
    }
}
