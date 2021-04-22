package com.bjpowernode.crm.settings.service.Impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {

    //利用代理机制，创建出UserDao接口的实现类
    private UserDao userdao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User findByNameAndPwd(String loginAct, String loginPwd, String ip) throws LoginException{
        System.out.println("L20...");
        Map<String,String> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userdao.findByNameAndPwd(map);

        if(user == null){
            throw new LoginException("账号密码错误");
        }
        //验证失效时间
        String expireTime = user.getExpireTime();
        String sysTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(sysTime) < 0){
            throw new LoginException("账号已失效");
        }

        //判定锁定状态
        String lockState = user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账号已锁定");
        }

        //判定ip地址
        String allowIps = user.getAllowIps();
        if(!allowIps.contains(ip)){
            throw new LoginException("IP地址受限");
        }

        return user;
    }
}
