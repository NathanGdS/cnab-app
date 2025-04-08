package br.com.nathangds.cnab_app;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private JobRepository jobRepository;

    @Bean
    Job job(Step step) {
        return new JobBuilder("job", this.jobRepository)
                .start(step)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    Step step(ItemReader<CnabTransaction> reader, ItemProcessor<CnabTransaction, Transaction> processor,
            ItemWriter<Transaction> write) {
        return new StepBuilder("step", jobRepository)
                .<CnabTransaction, Transaction>chunk(100, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(write)
                .build();
    }

    @Bean
    FlatFileItemReader<CnabTransaction> reader() {
        return new FlatFileItemReaderBuilder<CnabTransaction>()
                .name("reader")
                .resource(new FileSystemResource("files/CNAB.txt"))
                .fixedLength()
                .columns(
                        new Range(1, 1), new Range(2, 9),
                        new Range(10, 19), new Range(20, 30),
                        new Range(31, 42), new Range(43, 48),
                        new Range(49, 62), new Range(63, 80))
                .names("type", "date", "amount", "document", "cardNumber", "time", "companyOwner", "companyName")
                .targetType(CnabTransaction.class)
                .build();
    }

    @Bean
    ItemProcessor<CnabTransaction, Transaction> processor() {
        return item -> {
            return item.toTransaction();
        };
    }

    @Bean
    JdbcBatchItemWriter<Transaction> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Transaction>()
                .dataSource(dataSource)
                .sql(
                        """
                                    INSERT INTO transaction(
                                        tipo, data, valor, cpf, cartao,
                                         hora, dono_loja, nome_loja
                                        )  VALUES (:tipo, :data, :valor, :cpf, :cartao,
                                    :hora, :donoDaLoja, :nomeDaLoja)
                                """)
                .beanMapped()
                .build();

    }
}
