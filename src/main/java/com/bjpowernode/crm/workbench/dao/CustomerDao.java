package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer findByName(String customerName);

    int save(Customer c);

    List<String> getCustomerName(String name);

    int saveCus(Customer c);
}
