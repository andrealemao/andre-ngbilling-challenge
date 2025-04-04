package com.ngbilling.andredevtest.repository;

import com.ngbilling.andredevtest.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
