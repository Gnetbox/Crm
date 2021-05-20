package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.Impl.UserServiceImpl;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.Impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TransactionController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getServletPath();

        if("/workbench/transaction/getOwner.do".equals(path)){
           getOwner(request,response);
        }else if("/workbench/transaction/getCustomerName.do".equals(path)){ 
            getCustomerName(request,response);
        }
    }

    //自动补全数据
    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        String name = request.getParameter("name");
        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> csNames = cs.getCustomerName(name);
        PrintJson.printJsonObj(response,csNames);

    }

    //获取所有者填充数据
    private void getOwner(HttpServletRequest request, HttpServletResponse response) {

        UserService u = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = u.getUserList();
        PrintJson.printJsonObj(response,userList);
    }

}
