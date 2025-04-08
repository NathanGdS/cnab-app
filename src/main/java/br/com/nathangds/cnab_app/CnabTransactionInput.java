package br.com.nathangds.cnab_app;

import java.math.BigDecimal;

public record CnabTransactionInput(
        Integer type,
        String date,
        BigDecimal amount,
        Long document,
        String cardNumber,
        String time,
        String companyOwner,
        String companyName) {
}