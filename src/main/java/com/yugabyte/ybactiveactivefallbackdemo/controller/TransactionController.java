package com.yugabyte.ybactiveactivefallbackdemo.controller;


import com.yugabyte.ybactiveactivefallbackdemo.dao.TransactionRepository;
import com.yugabyte.ybactiveactivefallbackdemo.model.Customer;
import com.yugabyte.ybactiveactivefallbackdemo.util.GeneralUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SessionAttributes("randomCustomers")
@RequestMapping("/api")
public class TransactionController {
    private final TransactionRepository transactionRepository;
    @Autowired
    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/add")
    public ResponseEntity<String> writeData(@RequestParam(required = false, defaultValue = "1") int records){
        List<Customer> customers = GeneralUtility.getRandomCustomers(records);
        for(Customer customer: customers){
            try{
                transactionRepository.save(customer);
            }
            catch (Exception e){
                System.out.println("Error adding record: " + customer.toString());
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok("done");
    }




}
