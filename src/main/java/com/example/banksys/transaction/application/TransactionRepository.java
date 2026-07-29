package com.example.banksys.transaction.application;

import com.example.banksys.transaction.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByFromAccountAccountIdOrToAccountAccountIdOrderByTimestampDesc(Long fromAccountId, Long toAccountId);
}
