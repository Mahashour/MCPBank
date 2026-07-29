package com.example.banksys.client.interfaces.dto;

import com.example.banksys.client.domain.model.NationalId;
import com.example.banksys.client.domain.model.PhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record ClientCreateRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull NationalId nationalId,
        @Valid Set<PhoneNumber> phoneNumbers,
        String email) {}
