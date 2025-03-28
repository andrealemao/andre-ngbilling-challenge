package com.ngbilling.andredevtest.service;

import com.ngbilling.andredevtest.controller.dto.AccountDTO;
import com.ngbilling.andredevtest.model.Account;

import java.util.Optional;

public interface AccountService {

    AccountDTO save(AccountDTO dto);

    Optional<Account> findByNumber(int number);
}
