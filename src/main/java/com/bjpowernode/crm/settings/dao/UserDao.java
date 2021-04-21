package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.User;

import java.util.Map;

public interface UserDao {

    User findByNameAndPwd(Map<String, String> map);
}
