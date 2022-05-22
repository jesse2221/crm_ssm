package com.lsx.crm.workbench.service.impl;

import com.lsx.crm.workbench.domain.AfterSaleRemark;
import com.lsx.crm.workbench.mapper.AfterSaleRemarkMapper;
import com.lsx.crm.workbench.service.AfterSaleRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("afterSaleRemarkService")
public class AfterSaleRemarkServiceImpl implements AfterSaleRemarkService {
    @Autowired
    private AfterSaleRemarkMapper afterSaleRemarkMapper;

    @Override
    public List<AfterSaleRemark> queryAfterSaleRemarkForDetailByAfterSaleId(String afterSaleId) {
        return afterSaleRemarkMapper.selectAfterSaleRemarkForDetailByAfterSaleId(afterSaleId);
    }
}
