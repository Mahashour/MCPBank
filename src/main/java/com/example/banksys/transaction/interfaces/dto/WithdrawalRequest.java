package com.example.banksys.transaction.interfaces.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record WithdrawalRequest(
        @NotNull Long fromAccountId,
        @NotNull @Positive BigDecimal amount,
        String description
        ) {
}
