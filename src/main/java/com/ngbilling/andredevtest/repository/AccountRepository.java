package com.ngbilling.andredevtest.repository;

import com.ngbilling.andredevtest.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByNumber(int number);
}
