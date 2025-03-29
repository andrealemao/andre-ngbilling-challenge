package com.ngbilling.andredevtest.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record TransactionDTO(
        @JsonProperty("forma_pagamento")
        String paymentMethod,

        @JsonProperty("numero_conta")
        int number,

        @JsonProperty("valor")
        BigDecimal value
) {
}
