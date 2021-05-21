package com.bjpowernode.crm.workbench.service;

import java.util.List;

public interface CustomerService {
    List<String> getCustomerName(String name);

    String find(String tranCName,String createBy);
}
