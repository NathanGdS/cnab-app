package br.com.nathangds.cnab_app;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {
    private PlatformTransactionManager platformTransactionManager;

    public BatchConfig(PlatformTransactionManager transactionManager) {
        this.platformTransactionManager = transactionManager;
    }
}
