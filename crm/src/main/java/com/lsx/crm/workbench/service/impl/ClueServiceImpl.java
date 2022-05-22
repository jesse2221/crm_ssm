package com.lsx.crm.workbench.service.impl;

import com.lsx.crm.commons.constants.Constant;
import com.lsx.crm.commons.utils.DateUtils;
import com.lsx.crm.commons.utils.UUIDUtils;
import com.lsx.crm.settings.domain.User;
import com.lsx.crm.workbench.domain.*;
import com.lsx.crm.workbench.mapper.*;
import com.lsx.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private TranMapper tranMapper;
    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.inserClue(clue);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public void saveConvertClue(Map<String, Object> map) {
        String clueId= (String) map.get("clueId");
        User user = (User) map.get(Constant.SESSION_USER);
        String isCreateTran = (String) map.get("isCreateTran");
        //根据id查询线索的信息
        Clue clue=clueMapper.selectClueById(clueId);
        //把该线索中有关公司的信息切换到客户表中
        Customer customer = new Customer();
        customer.setAddress(clue.getAddress());
        customer.setContactSummary(clue.getContactSummary());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formateDateTime(new Date()));
        customer.setDescription(clue.getDescription());
        customer.setId(UUIDUtils.getUUID());
        customer.setName(clue.getCompany());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setOwner(user.getId());
        customer.setPhone(clue.getPhone());
        customer.setWebsite(clue.getWebsite());
        customerMapper.insertCustomer(customer);
        //把该线索中有关个人的信息转换到联系人表中
        Contacts contacts=new Contacts();
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formateDateTime(new Date()));
        contacts.setCustomerId(customer.getId());
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setId(UUIDUtils.getUUID());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());
        contactsMapper.insertContacts(contacts);

        //根据clueId查询该线索下所有的备注
        List<ClueRemark> clueremarkList = clueRemarkMapper.selectClueRemarkById(clueId);
        //如果该线索不为空，则将该线索下所有的备注转换到客户备注表中一份,
        //同时将线索中所有的备注也转换到联系人备注表中一份
        if(clueremarkList!=null && clueremarkList.size()>0){
            CustomerRemark customerRemark = null;
            ContactsRemark contactsRemark=null;
            List<CustomerRemark> customerRemarkList=new ArrayList<>();
            List<ContactsRemark> contactsRemarkList=new ArrayList<>();
            for(ClueRemark cr:clueremarkList){
                customerRemark = new CustomerRemark();
                customerRemark.setId(UUIDUtils.getUUID());
                customerRemark.setEditFlag(cr.getEditFlag());
                customerRemark.setEditBy(cr.getEditBy());
                customerRemark.setCustomerId(customer.getId());
                customerRemark.setCreateTime(cr.getCreateTime());
                customerRemark.setCreateBy(cr.getCreateBy());
                customerRemark.setEditTime(cr.getEditTime());
                customerRemark.setNoteContent(cr.getNoteContent());
                customerRemarkList.add(customerRemark);

                contactsRemark=new ContactsRemark();
                contactsRemark.setContactsId(contacts.getId());
                contactsRemark.setCreateBy(cr.getCreateBy());
                contactsRemark.setCreateTime(cr.getCreateTime());
                contactsRemark.setEditBy(cr.getEditBy());
                contactsRemark.setEditFlag(cr.getEditFlag());
                contactsRemark.setEditTime(cr.getEditTime());
                contactsRemark.setId(UUIDUtils.getUUID());
                contactsRemark.setNoteContent(cr.getNoteContent());
                contactsRemarkList.add(contactsRemark);
            }
            customerRemarkMapper.insertCustomerRemarkByList(customerRemarkList);
            contactsRemarkMapper.insertContactsRemarkByList(contactsRemarkList);
        }
        //根据clueId查询该线索和市场活动的关联关系
        //carList:ClueActivityRelationList
        List<ClueActivityRelation> carList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);
        if(carList!=null&&carList.size()>0){
            ContactsActivityRelation coar=null;
            List<ContactsActivityRelation> coarList=new ArrayList<>();
            //如果线索市场活动关联关系List不为空，把该线索和市场活动的关联关系转换到联系人和市场活动的关联关系表中
            for(ClueActivityRelation car:carList){
                coar=new ContactsActivityRelation();
                coar.setActivityId(car.getActivityId());
                coar.setContactsId(contacts.getId());
                coar.setId(UUIDUtils.getUUID());
                coarList.add(coar);
            }
            contactsActivityRelationMapper.insertContactsActivityRelationByList(coarList);
        }
        //如果需要创建交易，则往交易表中添加一条记录,还需要把该线索下所有的备注转换到交易备注表中一份
        if("true".equals(isCreateTran)){
            Tran tran = new Tran();
            tran.setActivityId((String) map.get("activityId"));
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DateUtils.formateDateTime(new Date()));
            tran.setCustomerId(customer.getId());
            tran.setExpectedDate((String) map.get("expectedDate"));
            tran.setId(UUIDUtils.getUUID());
            tran.setMoney((String) map.get("money"));
            tran.setName((String) map.get("name"));
            tran.setOwner(user.getId());
            tran.setStage((String) map.get("stage"));
            tranMapper.insertTran(tran);

            if(clueremarkList!=null&&clueremarkList.size()>0){
                TranRemark tr=null;
                List<TranRemark> tranRemarkList=new ArrayList<>();
                for (ClueRemark cr:clueremarkList){
                    tr=new TranRemark();
                    tr.setCreateBy(cr.getCreateBy());
                    tr.setCreateTime(cr.getCreateTime());
                    tr.setEditBy(cr.getEditBy());
                    tr.setEditFlag(cr.getEditFlag());
                    tr.setEditTime(cr.getEditTime());
                    tr.setId(UUIDUtils.getUUID());
                    tr.setNoteContent(cr.getNoteContent());
                    tr.setTranId(tran.getId());
                    tranRemarkList.add(tr);
                }
                tranRemarkMapper.insertTranRemarkByList(tranRemarkList);
            }
        }
        //删除该线索下所有的备注
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);
        //删除该线索和市场活动的关联关系
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);
        //删除该线索
        clueMapper.deleteClueById(clueId);
    }


    @Override
    public List<Clue> queryClueByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int queryCountOfClueByCondition(Map<String, Object> map) {
        return clueMapper.selectCountOfClueByCondition(map);
    }

    @Override
    public int deleteClueById(String[] ids) {
        return clueMapper.deleteClueByIds(ids);
    }

    @Override
    public Clue queryClueById(String id) {
        return clueMapper.selectClueById(id);
    }


    @Override
    public int saveEditClue(Clue clue) {
        return clueMapper.updateClue(clue);
    }
}
