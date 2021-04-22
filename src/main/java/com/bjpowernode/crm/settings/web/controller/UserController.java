package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.Impl.UserServiceImpl;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到用户控制器");

        String path = request.getServletPath();

        if("/settings/user/login.do".equals(path)) {
            login(request,response);
        }else if("/settings/user/xxx.do".equals(path)) {

        }

    }

    //登录方法校验账号密码
    private void login(HttpServletRequest request, HttpServletResponse response) {

        //获取输入的账号和密码
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");

        //将密码的铭文形式转为MD5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);

        //接收浏览器端的ip地址
        String ip = request.getRemoteAddr();

        //未来的业务层开发，统一使用代理类形态的接口对象
        UserService userService = (UserService)ServiceFactory.getService(new UserServiceImpl());

        try{
            User user = userService.findByNameAndPwd(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);
            PrintJson.printJsonFlag(response,true);
            
        }catch (Exception e){
            //一旦程序执行了catch块的信息，说明业务层为我们验证登录失败
            e.printStackTrace();
            String msg = e.getMessage();
            /*
            作为controller，需要为ajax请求提供多项信息
            可以有两种手段处理
            ①将多项信息打包为map,将map解析为json串
            ②创建一个vo
             */
            Map<String, Object> map = new HashMap<>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }
        

    }


}
