package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int saveTran(Tran tran);

    Tran getDetail(String id);

    int changeStage(Tran t);

    int getTotal();

    List<Map<String, Object>> getList();
}
