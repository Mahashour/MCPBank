package com.example.banksys.transaction.interfaces.dto;

import com.example.banksys.transaction.domain.model.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        TransactionType type,
        BigDecimal amount,
        Instant timestamp,
        String description,
        Long fromAccountId,
        Long toAccountId
) {
}
