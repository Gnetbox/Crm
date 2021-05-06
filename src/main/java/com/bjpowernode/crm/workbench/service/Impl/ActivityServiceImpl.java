package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PagenationVo;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);


    @Override
    public boolean save(Activity activity) {
        boolean flag = true;

        int count = activityDao.save(activity);

        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public PagenationVo<Activity> getActivity(Map<String, Object> map) {
       //获得市场列表活动总数
       int count = activityDao.getTotal(map);
       //获得对应页码的市场列表
       List<Activity> activityList = activityDao.getActivity(map);
       PagenationVo<Activity> vo = new PagenationVo<>();
       vo.setTotal(count);
       vo.setDataList(activityList);
       return vo;

    }

    @Override
    public boolean delete(String id) {
        System.out.println("进入deleteSER。。。");
        boolean flag = false;

        //先删remark`市场活动备注，再删市场活动
        //查询出需要删除的备注的数量
        int countRemark = activityRemarkDao.findCountRemark(id);

        //删除市场活动备注
        int countDelRemark = activityRemarkDao.deleteRemark(id);

        //如果活动备注成功删除，进行市场活动删除
        if(countRemark == countDelRemark){
            int count = activityDao.delete(id);
            if(count !=0){
                flag = true;
            }
        }

        return flag;
    }


}
