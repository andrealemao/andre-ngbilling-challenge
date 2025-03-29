package com.ngbilling.andredevtest.service;

import com.ngbilling.andredevtest.enums.PaymentMethod;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BalanceCalculator {

    public BigDecimal calculateFinalBalance(String paymentMethodString, BigDecimal originalBalance,
                                            BigDecimal value) {

        PaymentMethod paymentMethod = PaymentMethod.fromStringValue(paymentMethodString);

        assert paymentMethod != null;
        return switch (paymentMethod) {
            case PIX -> calculateBalanceWithPixRate(originalBalance, value);
            case CREDIT_CARD -> calculateBalanceWithCreditCardRate(originalBalance, value);
            case DEBIT_CARD -> calculateBalanceWithDebitCardRate(originalBalance, value);
        };
    }

    private BigDecimal calculateBalanceWithPixRate(BigDecimal originalBalance, BigDecimal value) {
        return originalBalance.subtract(value);
    }

    private BigDecimal calculateBalanceWithCreditCardRate(BigDecimal originalBalance, BigDecimal value) {
        return originalBalance.subtract(value.multiply(new BigDecimal("1.05")));
    }

    private BigDecimal calculateBalanceWithDebitCardRate(BigDecimal originalBalance, BigDecimal value) {
        return originalBalance.subtract(value.multiply(new BigDecimal("1.03")));
    }
}
