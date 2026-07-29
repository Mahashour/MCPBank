package com.example.banksys.transaction.domain.model;

import com.example.banksys.account.domain.model.Account;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(optional = true)
    private Account fromAccount;

    @ManyToOne(optional = true)
    private Account toAccount;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false, updatable = false)
    private Instant timestamp;

    private String description;

    public Transaction() {
    }

    public Transaction(Account fromAccount, Account toAccount, BigDecimal amount, TransactionType type, String description) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.type = type;
        this.timestamp = Instant.now(Clock.system(ZoneId.of("Africa/Cairo")));
        this.description = description;

        switch (type) {
            case TRANSFER -> {
                if (fromAccount == null || toAccount == null)
                    throw new IllegalArgumentException("Transfer must have a from and to account");
            }
            case DEPOSIT -> {
                if (toAccount == null) throw new IllegalArgumentException("Deposit must have a to account");
            }
            case WITHDRAWAL -> {
                if (fromAccount == null) throw new IllegalArgumentException("Withdrawal must have a from account");
            }
            case null, default -> throw new IllegalArgumentException("Unknown transaction");
        }


    }

    public String getDescription() {
        return description;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public UUID getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
