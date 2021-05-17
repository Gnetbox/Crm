package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PagenationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity activity);

    PagenationVo<Activity> getActivity(Map<String, Object> map);

    boolean delete(String id);

    Activity edit(String activityId);

    boolean update(Activity activity);

    Activity getDetail(String id);

    List<ActivityRemark> getRemark(String id);

    boolean delRemark(String id);

    boolean saveRemark(ActivityRemark activityRemark);

    boolean updateRemark(ActivityRemark activityRemark);

    List<Activity> activity(String clueId);

    List<Activity> getActivityBySearch(Map<String,Object> map);

    List<Activity> getActivityByName(String name);
}
