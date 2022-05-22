package com.lsx.crm.workbench.service;

import com.lsx.crm.workbench.domain.Contacts;
import com.lsx.crm.workbench.domain.CustomerRemark;

import java.util.List;

public interface CustomerRemarkService {

    List<CustomerRemark> queryCustomerRemarkForDatailByCustomerId(String id);


}
