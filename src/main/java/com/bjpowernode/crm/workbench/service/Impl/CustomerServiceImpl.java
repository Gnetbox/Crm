package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.service.CustomerService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public List<String> getCustomerName(String name) {

        List<String> cusNameList = customerDao.getCustomerName(name);
        return cusNameList;
    }

    @Override
    public String find(String tranCName,String createBy) {

        String customerId = null;

        String customerName = tranCName;
        Customer c = customerDao.findByName(customerName);

        //如果客户不存在，则添加客户
        int count = 0;
        if(c==null){
            c = new Customer();
            c.setId(UUIDUtil.getUUID());
            c.setName(customerName);
            c.setCreateBy(createBy);
            c.setCreateTime(DateTimeUtil.getSysTime());
            count += customerDao.saveCus(c);
            if(count ==1){
                customerId = c.getId();
            }
        }else {
            customerId = c.getId();
        }

        return customerId;
    }
}
