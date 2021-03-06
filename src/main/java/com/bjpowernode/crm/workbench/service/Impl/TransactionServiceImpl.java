package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TransactionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionServiceImpl implements TransactionService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean saveTran(Tran t) {

        boolean flg = true;
        //创建交易记录
        int count = tranDao.saveTran(t);
        if(count !=1){
            flg = false;
        }

        if(flg){
            //创建一条交易历史
            TranHistory ts = new TranHistory();
            ts.setId(UUIDUtil.getUUID());
            ts.setStage(t.getStage());
            ts.setMoney(t.getMoney());
            ts.setExpectedDate(t.getExpectedDate());
            ts.setCreateBy(t.getCreateBy());
            ts.setCreateTime(DateTimeUtil.getSysTime());
            ts.setTranId(t.getId());

            int count2 = tranHistoryDao.saveTranHis(ts);
            if(count2 !=1){
                flg = false;
            }
        }

        return flg;
    }

    @Override
    public Tran getDetail(String id) {
        return tranDao.getDetail(id);
    }

    @Override
    public List<TranHistory> showTranHistory(String tranId) {
        return tranHistoryDao.showTranHistory(tranId);
    }

    @Override
    public boolean changeStage(Tran t) {

        boolean flag = true;

        int count = tranDao.changeStage(t);
        if(count !=1){
            flag = false;
        }

        TranHistory ts = new TranHistory();
        ts.setId(UUIDUtil.getUUID());
        ts.setStage(t.getStage());
        ts.setTranId(t.getId());
        ts.setMoney(t.getMoney());
        ts.setExpectedDate(t.getExpectedDate());
        ts.setCreateTime(t.getEditTime());
        ts.setCreateBy(t.getEditBy());

        int count2 = tranHistoryDao.save(ts);
        if(count2 !=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {

        Map<String,Object> map = new HashMap<>();

        //取得total
        int total = tranDao.getTotal();
        //取得dataList
        List<Map<String,Object>> dataList = tranDao.getList();

        //将total，和dataList保存到map中
        map.put("total",total);
        map.put("dataList",dataList);
        return map;
    }


}
