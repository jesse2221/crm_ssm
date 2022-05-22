package com.lsx.crm.workbench.service.impl;

import com.lsx.crm.workbench.domain.CustomerRemark;
import com.lsx.crm.workbench.mapper.CustomerRemarkMapper;
import com.lsx.crm.workbench.service.CustomerRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("customerRemarkService")
public class CustomerRemarkServiceImpl implements CustomerRemarkService {

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Override
    public List<CustomerRemark> queryCustomerRemarkForDatailByCustomerId(String id) {
        return customerRemarkMapper.selectCustomerRemarkForDetailByCustomerId(id);
    }
}
