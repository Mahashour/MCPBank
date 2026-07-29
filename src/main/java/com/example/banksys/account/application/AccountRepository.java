package com.example.banksys.account.application;

import com.example.banksys.account.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByClient_BankId(UUID clientId);
}
