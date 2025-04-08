package br.com.nathangds.cnab_app;

import java.math.BigDecimal;

public record CnabTransaction(
        Integer tipo,
        String data,
        BigDecimal valor,
        Long cpf,
        String cartao,
        String hora,
        String donoDaLoja,
        String nomeDaLoja) {
}
