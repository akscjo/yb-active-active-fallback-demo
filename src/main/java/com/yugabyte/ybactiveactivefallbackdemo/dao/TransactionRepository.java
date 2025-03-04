package com.yugabyte.ybactiveactivefallbackdemo.dao;

import com.yugabyte.ybactiveactivefallbackdemo.config.RetryConfigProperties;
import com.yugabyte.ybactiveactivefallbackdemo.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Repository
public class TransactionRepository implements TransactionRepositoryInterface{
    private final JdbcTemplate primaryJdbcTemplate;
    private final JdbcTemplate secondaryJdbcTemplate;
    private static final String SQL_CUSTOMER_INSERT = "INSERT INTO customers (customer_id, company_name, customer_state) VALUES (?, ?, ?)";
    @Autowired
    private RetryTemplate retryTemplate;

    private final TransactionTemplate primaryTransactionTemplate;
    private final TransactionTemplate secondaryTransactionTemplate;

    @Autowired
    private RetryConfigProperties retryProperties;

    private static final Logger logger = LoggerFactory.getLogger(TransactionRepository.class);


    @Autowired
    public TransactionRepository(@Qualifier("primaryJdbcTemplate") JdbcTemplate primaryJdbcTemplate,
                                 @Qualifier("secondaryJdbcTemplate") JdbcTemplate secondaryJdbcTemplate,
                                 @Qualifier("primaryTransactionManager") PlatformTransactionManager primaryTxManager,
                                 @Qualifier("secondaryTransactionManager") PlatformTransactionManager secondaryTxManager) {
        this.primaryJdbcTemplate = primaryJdbcTemplate;
        this.secondaryJdbcTemplate = secondaryJdbcTemplate;
        this.primaryTransactionTemplate = new TransactionTemplate(primaryTxManager);
        this.secondaryTransactionTemplate = new TransactionTemplate(secondaryTxManager);
    }

    @Override
    public void save(Customer customer) {
        AtomicBoolean success = new AtomicBoolean(false);

        try {
            retryTemplate.execute(context -> {
                int retryCount = RetrySynchronizationManager.getContext().getRetryCount();
                System.out.printf("Attempting transaction on PRIMARY...[retryCount: %d, customerId: %s]\n", retryCount, customer.getCustomerId());

                primaryTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        try {
                            primaryJdbcTemplate.update(
                                    SQL_CUSTOMER_INSERT,
                                    customer.getCustomerId(),
                                    customer.getCompanyName(),
                                    customer.getCustomerState()
                            );
                            success.set(true); //  Mark as successful
                        } catch (Exception ex) {
                            System.out.printf("Transaction on PRIMARY failed, rolling back. Error: %s\n", ex.getMessage());
                            status.setRollbackOnly();
                            throw ex; //  Required to trigger retry
                        }
                    }
                });
                return null;
            });
        } catch (Exception primaryFailure) {
            System.out.printf("All retries on PRIMARY failed. Switching to SECONDARY...[customerId: %s]\n", customer.getCustomerId());
        }

        // Only try on SECONDARY if PRIMARY fully failed
        if (!success.get()) {
            try {
                retryTemplate.execute(context -> {
                    int retryCount = RetrySynchronizationManager.getContext().getRetryCount();
                    System.out.printf("Attempting transaction on SECONDARY...[retryCount: %d, customerId: %s]\n", retryCount, customer.getCustomerId());

                    secondaryTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
                        protected void doInTransactionWithoutResult(TransactionStatus status) {
                            try {
                                secondaryJdbcTemplate.update(
                                        SQL_CUSTOMER_INSERT,
                                        customer.getCustomerId(),
                                        customer.getCompanyName(),
                                        customer.getCustomerState()
                                );
                            } catch (Exception ex) {
                                System.out.printf("Transaction on SECONDARY failed, rolling back. Error: %s\n", ex.getMessage());
                                status.setRollbackOnly();
                                throw ex; // Required to trigger retry
                            }
                        }
                    });
                    return null;
                });
            } catch (Exception secondaryFailure) {
                System.out.printf("All retries on SECONDARY failed. Transaction failed. [customerId: %s]\n", customer.getCustomerId());
                throw new RuntimeException("Transaction failed on both PRIMARY and SECONDARY after max retries.", secondaryFailure);
            }
        }
    }



    @Override
    public List<Customer> getCustomers(){
        // TODO:
        String sql = "SELECT * from customers limit 100" ;
        return null;
    }
}
