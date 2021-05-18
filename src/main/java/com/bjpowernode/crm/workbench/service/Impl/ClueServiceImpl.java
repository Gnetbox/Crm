package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class ClueServiceImpl implements ClueService {

    //线索相关
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    //客户相关
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    //联系人相关
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    //交易相关
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


    @Override
    public boolean save(Clue c) {

        boolean flag = true;
        int count = clueDao.save(c);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Clue detail(String id) {

        Clue clue = clueDao.detail(id);
        return clue;
    }

    @Override
    public boolean unbund(String id) {

        boolean flag = true;
        int count = clueActivityRelationDao.unbund(id);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean relate(String clueId, String[] ids) {

        boolean flag = true;

        ClueActivityRelation r = new ClueActivityRelation();
        r.setClueId(clueId);

        for (String id : ids) {
            r.setId(UUIDUtil.getUUID());
            r.setActivityId(id);
            int count = clueActivityRelationDao.relate(r);
            if (count != 1) {
                flag = false;
            }
        }


        return flag;
    }

    @Override
    public boolean change(String clueId, Tran tran, String createBy) {

        boolean flg = true;
        String createTime = DateTimeUtil.getSysTime();

        //1.通过clueId 获取线索信息
        Clue clue = clueDao.getById(clueId);

        //2.通过线索对象提前客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在!）
        String customerName = clue.getCompany();
        Customer c = customerDao.findByName(customerName);
        if (c == null) {

            c = new Customer();
            c.setId(UUIDUtil.getUUID());
            c.setOwner(clue.getOwner());
            c.setName(clue.getCompany());
            c.setWebsite(clue.getWebsite());
            c.setPhone(clue.getPhone());
            c.setCreateBy(createBy);
            c.setCreateTime(createTime);
            c.setContactSummary(clue.getContactSummary());
            c.setNextContactTime(clue.getNextContactTime());
            c.setDescription(clue.getDescription());
            c.setAddress(clue.getAddress());
        }

        int countCustomer = customerDao.save(c);
        if(countCustomer !=1){
            flg = false;
        }

        Contacts contacts = null;
        //3.通过线索对象提取 联系人信息，保存联系人
        if (flg) {
            //设置联系人信息
            contacts = new Contacts();
            contacts.setId(UUIDUtil.getUUID());
            contacts.setOwner(clue.getOwner());
            contacts.setSource(clue.getSource());
            contacts.setCustomerId(c.getId());
            contacts.setFullname(clue.getFullname());
            contacts.setAppellation(clue.getAppellation());
            contacts.setEmail(clue.getEmail());
            contacts.setMphone(clue.getMphone());
            contacts.setJob(clue.getJob());
            contacts.setCreateBy(createBy);
            contacts.setCreateTime(createTime);
            contacts.setDescription(clue.getDescription());
            contacts.setContactSummary(clue.getContactSummary());
            contacts.setNextContactTime(clue.getNextContactTime());
            contacts.setAddress(clue.getAddress());

            int countContacts = contactsDao.saveContacts(contacts);
            if (countContacts != 1) {
                flg = false;
            }
        }

            List<ClueRemark> clueRemarkList = null;
            //4.线索备注转换到 客户备注以及联系人备注
            if(flg){
                //取得线索备注信息list
                clueRemarkList = clueRemarkDao.findNote(clueId);
                int countCustomerRemark = 0;
                int countContactsRemark = 0;

                //取得每一条备注信息对象
                for (ClueRemark clueRemark : clueRemarkList) {
                    //获得备注信息
                    String noteContent = clueRemark.getNoteContent();

                    CustomerRemark customerRemark = new CustomerRemark();
                    customerRemark.setId(UUIDUtil.getUUID());
                    customerRemark.setNoteContent(noteContent);
                    customerRemark.setCreateBy(createBy);
                    customerRemark.setCreateTime(createTime);
                    customerRemark.setEditFlag("0");
                    customerRemark.setCustomerId(c.getId());

                    countCustomerRemark = customerRemarkDao.saveCustomerNote(customerRemark);

                    ContactsRemark contactsRemark = new ContactsRemark();
                    contactsRemark.setId(UUIDUtil.getUUID());
                    contactsRemark.setNoteContent(noteContent);
                    contactsRemark.setCreateBy(createBy);
                    contactsRemark.setCreateTime(createTime);
                    contactsRemark.setEditFlag("0");
                    contactsRemark.setContactsId(contacts.getId());

                    countContactsRemark = contactsRemarkDao.saveContactsNote(contactsRemark);

                    if(countCustomerRemark !=1 ||countContactsRemark !=1){
                        flg = false;
                    }
                }
            }

            //5.“tbl_clue_activity_relation"转换到 ”tbl_contacts_activity_relation“
            List<ClueActivityRelation> car = clueActivityRelationDao.findByClueId(clueId);
            for (ClueActivityRelation clueActivityRelation : car) {

                String activityId = clueActivityRelation.getActivityId();
                ContactsActivityRelation carr = new ContactsActivityRelation();
                carr.setId(UUIDUtil.getUUID());
                carr.setContactsId(contacts.getId());
                carr.setActivityId(activityId);

                int carrCount = contactsActivityRelationDao.saveCAR(carr);
                if(carrCount !=1){
                    flg = false;
                }
            }

            //6.如果有创建交易的需求，创建一条交易记录
            if(tran !=null){
                tran.setSource(clue.getSource());
                tran.setOwner(clue.getOwner());
                tran.setNextContactTime(clue.getNextContactTime());
                tran.setDescription(clue.getDescription());
                tran.setCustomerId(c.getId());
                tran.setContactSummary(contacts.getContactSummary());
                tran.setContactsId(contacts.getId());

                int countTran = tranDao.saveTran(tran);
                if(countTran !=1){
                    flg = false;
                }

                //7.如果创建了交易，则创建一条该交易下的交易历史
                TranHistory tranHistory = new TranHistory();
                tranHistory.setId(UUIDUtil.getUUID());
                tranHistory.setStage(tran.getStage());
                tranHistory.setMoney(tran.getMoney());
                tranHistory.setExpectedDate(tran.getExpectedDate());
                tranHistory.setCreateTime(createTime);
                tranHistory.setCreateBy(createBy);
                tranHistory.setTranId(tran.getId());

                int countTranHis = tranHistoryDao.saveTranHis(tranHistory);
                if(countTranHis !=1){
                    flg = false;
                }
            }

            //8.删除线索备注
           /* int countDelClueR = clueRemarkDao.del(clueId);
            if(countDelClueR != clueRemarkList.size()){
                flg = false;
            }*/

            //9.删除线索和市场活动的关系
           /* int countDelCar = clueActivityRelationDao.del(clueId);
            if(countDelCar !=car.size()){
                flg = false;
            }*/

            //10.删除线索
           /*int countDelClue =  clueDao.del(clueId);
           if(countDelClue !=1){
               flg = false;
           }
*/

        return flg;

    }
}