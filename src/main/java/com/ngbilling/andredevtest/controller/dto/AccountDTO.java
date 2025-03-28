package com.ngbilling.andredevtest.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record AccountDTO(
        @JsonProperty("numero_conta")
        int number,

        @JsonProperty("saldo")
        @Min(value = 0, message = "Can't have negative balance")
        BigDecimal balance
) {

}
