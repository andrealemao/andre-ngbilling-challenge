package com.ngbilling.andredevtest.service.impl;

import com.ngbilling.andredevtest.controller.dto.AccountDTO;
import com.ngbilling.andredevtest.model.Account;
import com.ngbilling.andredevtest.repository.AccountRepository;
import com.ngbilling.andredevtest.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private final AccountRepository repository;

    public AccountServiceImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public AccountDTO save(AccountDTO dto) {
        Account account = new Account(dto.number(), dto.balance());
        repository.save(account);

        return new AccountDTO(account.getNumber(), account.getBalance());
    }

    @Override
    public Optional<Account> findByNumber(int number) {
        return repository.findByNumber(number);
    }
}
