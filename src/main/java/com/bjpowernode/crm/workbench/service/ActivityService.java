package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PagenationVo;
import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity activity);

    PagenationVo<Activity> getActivity(Map<String, Object> map);

    boolean delete(String id);
}
