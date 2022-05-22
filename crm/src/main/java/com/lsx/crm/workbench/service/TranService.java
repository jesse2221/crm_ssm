package com.lsx.crm.workbench.service;

import com.lsx.crm.workbench.domain.Contacts;
import com.lsx.crm.workbench.domain.Customer;
import com.lsx.crm.workbench.domain.FunnelVo;
import com.lsx.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {
    void saveCreateTran(Map<String,Object> map);

    Tran queryTranForDetailById(String id);

    List<FunnelVo> queryCountOfTranGroupByStage();

    List<Tran> queryTranByConditionForPage(Map<String,Object> map);

    int queryCountOfTranByCondition(Map<String,Object> map);

    int deleteTranById(String[] ids);

    Tran queryTranById(String id);

    Tran queryTranById2(String id);

    int saveEditTran(Map<String, Object> map);
}
