package com.example.banksys.account.domain.model;

import com.example.banksys.client.domain.model.*;
import com.example.banksys.client.infrastructure.NationalIdConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Client client;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Version
    private Long version;

    public Account(){

    }

    public Account(Client client, BigDecimal initialBalance){
        this.client = client;
        this.balance = initialBalance;
    }

    @PrePersist
    private void onCreate(){
        this.createdAt = LocalDateTime.now(ZoneId.of("Africa/Cairo"));
    }

    public Long getAccountId() {
        return accountId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client){this.client = client;}

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getVersion() {
        return version;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}