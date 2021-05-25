package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;

public interface TranDao {

    int saveTran(Tran tran);

    Tran getDetail(String id);

    int changeStage(Tran t);

    List<Tran> getCharts();
}
