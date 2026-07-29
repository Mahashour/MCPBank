package com.example.banksys.account.interfaces.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountCreateRequest(
        @NotNull UUID clientId,
        BigDecimal initialBalance
) {}
