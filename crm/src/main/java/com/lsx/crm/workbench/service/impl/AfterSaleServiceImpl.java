package com.lsx.crm.workbench.service.impl;

import com.lsx.crm.workbench.domain.AfterSale;
import com.lsx.crm.workbench.mapper.AfterSaleMapper;
import com.lsx.crm.workbench.service.AfterSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service("afterSaleService")
public class AfterSaleServiceImpl implements AfterSaleService {

    @Autowired
    private AfterSaleMapper afterSaleMapper;

    @Override
    public List<AfterSale> queryAfterSalrByConditionForPage(Map<String, Object> map) {
        return afterSaleMapper.selectAfterSaleByConditionForPage(map);
    }

    @Override
    public int queryCountOfAfterSaleByCondition(Map<String, Object> map) {
        return afterSaleMapper.selectCountOfAfterSaleByCondition(map);
    }

    @Override
    public void saveCreateAfterSale(AfterSale afterSale) {
        afterSaleMapper.insertAfterSale(afterSale);
    }

    @Override
    public AfterSale queryAfterSaleForDetailById(String id) {
        return afterSaleMapper.selectAfterSaleForDetailById(id);
    }

    @Override
    public int deleteAfterSaleById(String[] ids) {
        return afterSaleMapper.deleteAfterSaleByIds(ids);
    }
}
