CREATE TABLE IF NOT EXISTS transaction(
    id SERIAL PRIMARY KEY,
    tipo INT,
    data DATE,
    valor DECIMAL,
    cpf BIGINT,
    cartao VARCHAR(255),
    hora TIME,
    dono_loja VARCHAR(255),
    nome_loja VARCHAR(255)
);