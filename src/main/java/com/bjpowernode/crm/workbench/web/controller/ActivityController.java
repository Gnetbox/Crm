package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.Impl.UserServiceImpl;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PagenationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.Impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到市场活动控制器");

        String path = request.getServletPath();

        if("/workbench/activity/getUserList.do".equals(path)) {
            getUserList(request,response);
        }else if("/workbench/activity/save.do".equals(path)) {
            save(request,response);
        }else if("/workbench/activity/getActivity.do".equals(path)){
            getActivity(request,response);
        }else if("/workbench/activity/delete.do".equals(path)){
            delete(request,response);
        }else if("/workbench/activity/edit.do".equals(path)){
            edit(request,response);
        }else if("/workbench/activity/update.do".equals(path)){
            update(request,response);
        } else if("/workbench/activity/getDetail.do".equals(path)){
            getDetail(request,response);
        }

    }

    //点击名字，进入到市场活动详情页面部分
    private void getDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到获取详情..");
        String id = request.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity = activityService.getDetail(id);
        request.setAttribute("a",activity);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);

    }

    //获得所有用户的信息
    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        UserService userService = (UserService)ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();
        PrintJson.printJsonObj(response,userList);
    }

    //保存创建市场活动信息
    private void save(HttpServletRequest request, HttpServletResponse response) {

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate  = request.getParameter("startDate");
        String endDate= request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description  = request.getParameter("description");
        //创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人，当前登录用户
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        System.out.println("activity :"+activity);

        ActivityService activityService = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = activityService.save(activity);
        PrintJson.printJsonFlag(response,flag);
    }

    //获取创建活动列表信息
    private void getActivity(HttpServletRequest request, HttpServletResponse response) {

        //查询条件
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        //选定页码数
        String pageNumStr = request.getParameter("pageNum");
        int pageNum = Integer.parseInt(pageNumStr) ;
        //每页展示信息条数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.parseInt(pageSizeStr);
        //计算limit(startIndex,pageSize)
        int startIndex = (pageNum-1)*pageSize;

        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("startIndex",startIndex);
        map.put("pageSize",pageSize);

        ActivityService activityService = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        PagenationVo<Activity> vo = activityService.getActivity(map);
        PrintJson.printJsonObj(response,vo);

    }

    //进行删除操作
    private void delete(HttpServletRequest request, HttpServletResponse response) {

        String[] ids = request.getParameterValues("ids");
        boolean flag = false;
        for (String id : ids) {
            System.out.println("L127  :"+id);
            ActivityService service = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
            flag = service.delete(id);
            if(flag != true){
                break;
            }
        }

        PrintJson.printJsonFlag(response,flag);

    }

    //点击修改按钮，获取到市场详情信息
    private void edit(HttpServletRequest request, HttpServletResponse response) {

        String activityId = request.getParameter("activityId");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity = activityService.edit(activityId);
        System.out.println("L143 :"+activity);
        PrintJson.printJsonObj(response,activity);
    }

    //修改市场活动
    private void update(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入update...");
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        System.out.println("L153-owner  :"+owner);
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String editBy = request.getParameter("editBy");

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditBy(editBy);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = activityService.update(activity);
        PrintJson.printJsonFlag(response,flag);

    }
}


