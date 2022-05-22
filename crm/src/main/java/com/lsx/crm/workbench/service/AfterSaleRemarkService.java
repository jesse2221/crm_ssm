package com.lsx.crm.workbench.service;

import com.lsx.crm.workbench.domain.ActivityRemark;
import com.lsx.crm.workbench.domain.AfterSaleRemark;

import java.util.List;

public interface AfterSaleRemarkService {
    List<AfterSaleRemark> queryAfterSaleRemarkForDetailByAfterSaleId(String afterSaleId);
}
