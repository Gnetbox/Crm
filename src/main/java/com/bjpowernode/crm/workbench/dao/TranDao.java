package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Tran;

public interface TranDao {

    int saveTran(Tran tran);

    Tran getDetail(String id);
}
