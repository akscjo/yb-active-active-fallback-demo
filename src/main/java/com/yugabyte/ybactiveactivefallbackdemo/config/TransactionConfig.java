package com.yugabyte.ybactiveactivefallbackdemo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class TransactionConfig {

    @Bean
    public TransactionTemplate primaryTransactionTemplate(@Qualifier("primaryTransactionManager") PlatformTransactionManager primaryTxManager) {
        return new TransactionTemplate(primaryTxManager);
    }

    @Bean
    public TransactionTemplate secondaryTransactionTemplate(@Qualifier("secondaryTransactionManager") PlatformTransactionManager secondaryTxManager) {
        return new TransactionTemplate(secondaryTxManager);
    }


}
