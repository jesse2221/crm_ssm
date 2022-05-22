package com.lsx.crm.workbench.service;

import com.lsx.crm.workbench.domain.AfterSale;

import java.util.List;
import java.util.Map;

public interface AfterSaleService {
    List<AfterSale> queryAfterSalrByConditionForPage(Map<String,Object> map);

    int queryCountOfAfterSaleByCondition(Map<String,Object> map);

    void saveCreateAfterSale(AfterSale afterSale);

    AfterSale queryAfterSaleForDetailById(String id);

    int deleteAfterSaleById(String[] ids);
}
