package com.example.banksys.account.interfaces.dto;

import com.example.banksys.account.domain.model.AccountStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponse(
        Long accountId,
        UUID clientId,
        BigDecimal balance,
        AccountStatus status
) {
}
