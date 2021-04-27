package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    User findByNameAndPwd(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
