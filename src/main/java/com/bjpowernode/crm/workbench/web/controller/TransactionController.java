package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.Impl.UserServiceImpl;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.Impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.Impl.TransactionServiceImpl;
import com.bjpowernode.crm.workbench.service.TransactionService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TransactionController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getServletPath();

        if("/workbench/transaction/getOwner.do".equals(path)){
           getOwner(request,response);
        }else if("/workbench/transaction/getCustomerName.do".equals(path)){ 
            getCustomerName(request,response);
        }else if("/workbench/transaction/saveTran.do".equals(path)){
            saveTran(request,response);
        }else if("/workbench/transaction/detail.do".equals(path)){
            detail(request,response);
        }
    }

    //获取detail信息
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        TransactionService ts = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        Tran t = ts.getDetail(id);
        request.setAttribute("t",t);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);

    }

    //保存交易信息
    private void saveTran(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //拿到输入客户名称，去后台判断客户是否存在，如果存在就拿到CustomerId,如果不存在，则创建客户并拿到Id
        String tranCName = request.getParameter("tranCName");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        String customerId = cs.find(tranCName,createBy);

        String tranOwner = request.getParameter("tranOwner");
        String tranMoney = request.getParameter("tranMoney");
        String tranName = request.getParameter("tranName");
        String tranEDate = request.getParameter("tranEDate");
        String tranStage = request.getParameter("tranStage");
        String tranType = request.getParameter("tranType");
        String tranSource = request.getParameter("tranSource");
        String tranASrc = request.getParameter("activitySrc");
        String tranCN = request.getParameter("contactsName");
        String tranDescription = request.getParameter("tranDescription");
        String tranCSummary = request.getParameter("tranCSummary");
        String tranCTime = request.getParameter("tranCTime");

        Tran t = new Tran();
        t.setId(UUIDUtil.getUUID());
        t.setOwner(tranOwner);
        t.setMoney(tranMoney);
        t.setName(tranName);
        t.setExpectedDate(tranEDate);
        t.setCustomerId(customerId);
        t.setStage(tranStage);
        t.setType(tranType);
        t.setSource(tranSource);
        t.setActivityId(tranASrc);
        t.setContactsId(tranCN);
        t.setCreateBy(createBy);
        t.setCreateTime(DateTimeUtil.getSysTime());
        t.setDescription(tranDescription);
        t.setContactSummary(tranCSummary);
        t.setNextContactTime(tranCTime);

        TransactionService ts = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        boolean flag = ts.saveTran(t);

        //如果添加成功，跳转到index.jsp
        if(flag){
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
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
