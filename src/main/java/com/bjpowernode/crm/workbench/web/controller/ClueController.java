package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.Impl.UserServiceImpl;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.Impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.Impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到线索控制器..");
        String path = request.getServletPath();

        if(("/workbench/clue/getUserList.do").equals(path)){
            getUserList(request,response);
        }else if(("/workbench/clue/save.do").equals(path)){
            save(request,response);
        }else if(("/workbench/clue/detail.do").equals(path)){
            detail(request,response);
        }else if(("/workbench/clue/activity.do").equals(path)){
            activity(request,response);
        }else if(("/workbench/clue/del.do").equals(path)){
            del(request,response);
        }
        else if(("/workbench/clue/getActivityBySearch.do").equals(path)){
            getActivityBySearch(request,response);
        }else if(("/workbench/clue/relate.do").equals(path)){
            relate(request,response);
        }else if(("/workbench/clue/getActivityByName.do").equals(path)){
            getActivityByName(request,response);
        }
    }

    //获取市场活动
    private void getActivityByName(HttpServletRequest request, HttpServletResponse response) {

        String name = request.getParameter("name");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activityList = activityService.getActivityByName(name);
        PrintJson.printJsonObj(response,activityList);
    }

    //建立关联
    private void relate(HttpServletRequest request, HttpServletResponse response) {

        String clueId = request.getParameter("clueId");
        String[] ids = request.getParameterValues("ids");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = clueService.relate(clueId,ids);

        PrintJson.printJsonFlag(response,flag);


    }

    //查找市场活动
    private void getActivityBySearch(HttpServletRequest request, HttpServletResponse response) {

        String name = request.getParameter("name");
        String clueId = request.getParameter("clueId");
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("clueId",clueId);

        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activity = service.getActivityBySearch(map);
        PrintJson.printJsonObj(response,activity);

    }

    //解除关联关系
    private void del(HttpServletRequest request, HttpServletResponse response) {

        String id = request.getParameter("id");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = clueService.unbund(id);
        PrintJson.printJsonFlag(response,flag);
    }

    //获取关联市场活动列表
    private void activity(HttpServletRequest request, HttpServletResponse response) {

        String clueId = request.getParameter("clueId");
        ActivityService a = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activityList = a.activity(clueId);
        PrintJson.printJsonObj(response,activityList);
    }

    //跳转线索详情
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue  = clueService.detail(id);
        request.setAttribute("c",clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);

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
