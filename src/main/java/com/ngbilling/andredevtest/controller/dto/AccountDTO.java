package com.ngbilling.andredevtest.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountDTO(
        @JsonProperty("numero_conta")
        int number,

        @NotNull(message = "O saldo da conta não pode ser nulo")
        @Min(value = 0, message = "Saldo da conta não pode ser negativo")
        @JsonProperty("saldo")
        BigDecimal balance
) {

}
