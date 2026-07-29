package com.example.banksys.transaction.interfaces.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DepositRequest(
        @NotNull Long toAccountId,
        @NotNull @Positive BigDecimal amount,
        String description
) {
}
