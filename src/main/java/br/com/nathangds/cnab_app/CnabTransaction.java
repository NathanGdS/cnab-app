package br.com.nathangds.cnab_app;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public record CnabTransaction(
        Integer type,
        String date,
        BigDecimal amount,
        Long document,
        String cardNumber,
        String time,
        String companyOwner,
        String companyName) {

    Transaction toTransaction() throws ParseException {
        var dateFormat = new SimpleDateFormat("yyyyMMdd");
        var hourFormat = new SimpleDateFormat("HHmmss");

        return new Transaction(document, type,
                new Date(dateFormat.parse(date).getTime()),
                amount.divide(BigDecimal.valueOf(100)), document, companyOwner,
                new Time(hourFormat.parse(time).getTime()), companyName, cardNumber);
    }
}
