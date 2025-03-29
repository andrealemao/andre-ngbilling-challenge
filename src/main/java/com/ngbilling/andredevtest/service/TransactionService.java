package com.ngbilling.andredevtest.service;

import com.ngbilling.andredevtest.controller.dto.TransactionDTO;
import com.ngbilling.andredevtest.model.Account;
import com.ngbilling.andredevtest.model.Transaction;

import java.util.Optional;

public interface TransactionService {

    Transaction save(TransactionDTO dto, Account account);
}
