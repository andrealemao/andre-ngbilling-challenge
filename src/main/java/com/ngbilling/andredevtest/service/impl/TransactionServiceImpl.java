package com.ngbilling.andredevtest.service.impl;

import com.ngbilling.andredevtest.controller.dto.TransactionDTO;
import com.ngbilling.andredevtest.model.Account;
import com.ngbilling.andredevtest.model.Transaction;
import com.ngbilling.andredevtest.repository.TransactionRepository;
import com.ngbilling.andredevtest.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private final TransactionRepository repository;

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Transaction save(TransactionDTO dto, Account account) {
        Transaction transaction = new Transaction(dto.paymentMethod(), account);
        repository.save(transaction);

        return transaction;
    }
}
