package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {

    int unbund(String id);

    int relate(ClueActivityRelation r);

    List<ClueActivityRelation> findByClueId(String clueId);

    int del(String clueId);
}
