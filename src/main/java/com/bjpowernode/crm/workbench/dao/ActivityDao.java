package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    int save(Activity activity);

    List<Activity> getActivity(Map<String, Object> map);

    int getTotal(Map<String, Object> map);

    int delete(String id);

    Activity edit(String activityId);

    int update(Activity activity);

    Activity getDetail(String id);

    List<Activity> activity(String clueId);
}
