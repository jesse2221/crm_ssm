package com.lsx.crm.workbench.service.impl;

import com.lsx.crm.commons.constants.Constant;
import com.lsx.crm.commons.utils.DateUtils;
import com.lsx.crm.commons.utils.UUIDUtils;
import com.lsx.crm.settings.domain.User;
import com.lsx.crm.workbench.domain.Customer;
import com.lsx.crm.workbench.domain.FunnelVo;
import com.lsx.crm.workbench.domain.Tran;
import com.lsx.crm.workbench.domain.TranHistory;
import com.lsx.crm.workbench.mapper.CustomerMapper;
import com.lsx.crm.workbench.mapper.TranHistoryMapper;
import com.lsx.crm.workbench.mapper.TranMapper;
import com.lsx.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("tranService")
public class TranServiceImpl implements TranService {
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private TranMapper tranMapper;
    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Override
    public void saveCreateTran(Map<String, Object> map) {
        User user= (User) map.get(Constant.SESSION_USER);
        String customerName= (String) map.get("customerName");
        Customer customer = customerMapper.queryCustomerByName(customerName);
        if(customer==null){
            customer=new Customer();
            customer.setOwner(user.getId());
            customer.setName(customerName);
            customer.setId(UUIDUtils.getUUID());
            customer.setCreateTime(DateUtils.formateDateTime(new Date()));
            customer.setCreateBy(user.getId());
            customerMapper.insertCustomer(customer);
        }
        //保存创建交易
        Tran tran = new Tran();
        tran.setStage((String) map.get(("stage")));
        tran.setOwner((String) map.get("owner"));
        tran.setName((String) map.get("name"));
        tran.setMoney((String) map.get("money"));
        tran.setId(UUIDUtils.getUUID());
        tran.setExpectedDate((String) map.get("expectedDate"));
        tran.setCustomerId(customer.getId());
        tran.setCreateTime(DateUtils.formateDateTime(new Date()));
        tran.setCreateBy(user.getId());
        tran.setContactsId((String) map.get("contactsId"));
        tran.setActivityId((String) map.get("activityId"));
        tran.setContactSummary((String) map.get("contactSummary"));
        tran.setDescription((String) map.get("description"));
        tran.setNextContactTime((String) map.get("nextContactTime"));
        tran.setSource((String) map.get("source"));
        tran.setType((String) map.get("type"));
        tranMapper.insertTran(tran);
        TranHistory tranHistory = new TranHistory();
        tranHistory.setCreateBy(user.getId());
        tranHistory.setCreateTime(DateUtils.formateDateTime(new Date()));
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setId(UUIDUtils.getUUID());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setTranId(tran.getId());
        tranHistoryMapper.insertTranHistory(tranHistory);
    }

    @Override
    public Tran queryTranForDetailById(String id) {
        return tranMapper.selectTranForDetailById(id);
    }

    @Override
    public List<FunnelVo> queryCountOfTranGroupByStage() {
        return tranMapper.selectCountOfTranGroupByStage();
    }

    @Override
    public List<Tran> queryTranByConditionForPage(Map<String, Object> map) {
        return tranMapper.selectTranByConditionForPage(map);
    }

    @Override
    public int queryCountOfTranByCondition(Map<String, Object> map) {
        return tranMapper.selectCountOfTranByCondition(map);
    }

    @Override
    public int deleteTranById(String[] ids) {
        return tranMapper.deleteTranByIds(ids);
    }

    @Override
    public Tran queryTranById(String id) {
        return tranMapper.selectTranById(id);
    }

    @Override
    public Tran queryTranById2(String id) {
        return tranMapper.selectTranById2(id);
    }

    @Override
    public int saveEditTran(Map<String, Object> map) {
        return tranMapper.updateTran(map);
    }
}
