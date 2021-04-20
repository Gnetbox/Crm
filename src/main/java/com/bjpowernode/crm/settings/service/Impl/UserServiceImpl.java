package com.bjpowernode.crm.settings.service.Impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

public class UserServiceImpl implements UserService {

    //利用代理机制，创建出UserDao接口的实现类
    private UserDao userdao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
}
