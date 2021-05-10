package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {

    int findCountRemark(String id);

    int deleteRemark(String id);

    List<ActivityRemark> getRemark(String id);

    int delRemark(String id);
}
