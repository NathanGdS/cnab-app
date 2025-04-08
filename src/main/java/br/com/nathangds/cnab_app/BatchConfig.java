package br.com.nathangds.cnab_app;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .build();
    }
}
