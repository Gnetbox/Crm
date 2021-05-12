package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.Impl.UserServiceImpl;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.Impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到线索控制器..");
        String path = request.getServletPath();

        if(("/workbench/clue/getUserList.do").equals(path)){
            getUserList(request,response);
        }else if(("/workbench/clue/save.do").equals(path)){
            save(request,response);
        }
    }

    //保存线索
    private void save(HttpServletRequest request, HttpServletResponse response) {

        String clueOwner = request.getParameter("clueOwner");
        String company = request.getParameter("company");
        String call = request.getParameter("call");
        String name = request.getParameter("name");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String status = request.getParameter("status");
        String source = request.getParameter("source");
        String describe = request.getParameter("describe");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();
        c.setId(UUIDUtil.getUUID());
        c.setFullname(name);
        c.setAppellation(call);
        c.setOwner(clueOwner);
        c.setCompany(company);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setState(status);
        c.setSource(source);
        c.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        c.setCreateTime(DateTimeUtil.getSysTime());
        c.setDescription(describe);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);
        System.out.println("L74 :"+c);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = clueService.save(c);
        PrintJson.printJsonFlag(response,flag);

    }

    //拿到userList
    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = us.getUserList();
        PrintJson.printJsonObj(response,userList);
    }
}
