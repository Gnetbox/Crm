package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TransactionService {
    boolean saveTran(Tran t);

    Tran getDetail(String id);

    List<TranHistory> showTranHistory(String tranId);

    boolean changeStage(Tran t);

    Map<String, Object> getCharts();
}
