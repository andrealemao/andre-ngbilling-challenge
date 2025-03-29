package com.ngbilling.andredevtest.enums;

public enum PaymentMethod {
    PIX("P"),
    CREDIT_CARD("C"),
    DEBIT_CARD("D");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PaymentMethod fromStringValue(String value) {
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            if (paymentMethod.value.equals(value)) {
                return paymentMethod;
            }
        }

        return null;
    }
}
