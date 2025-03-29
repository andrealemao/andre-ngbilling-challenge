package com.ngbilling.andredevtest.repository;

import com.ngbilling.andredevtest.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByNumber(int number);
}
