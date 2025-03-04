package com.yugabyte.ybactiveactivefallbackdemo.dao;


import com.yugabyte.ybactiveactivefallbackdemo.model.Customer;

import java.util.List;

public interface TransactionRepositoryInterface {
    void save(Customer customer);
    List<Customer> getCustomers();

}
