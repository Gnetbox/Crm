package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int saveTranHis(TranHistory tranHistory);

    List<TranHistory> showTranHistory(String tranId);

    int save(TranHistory ts);
}
