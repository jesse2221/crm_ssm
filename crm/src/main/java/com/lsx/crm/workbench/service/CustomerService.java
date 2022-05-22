package com.lsx.crm.workbench.service;

import com.lsx.crm.workbench.domain.Clue;
import com.lsx.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    List<String> queryCustomerNameByName(String name);

    Customer queryCustomerByName(String customerName);

    int saveCreateCustomer(Customer customer);

    List<Customer> queryCustomerByConditionForPage(Map<String,Object> map);

    int queryCountOfClueByCondition(Map<String,Object> map);

    Customer queryCustomerForDetailById(String id);

    int deleteCustomerById(String[] ids);

    Customer queryCustomerById(String id);

    int saveEditCustomer(Customer customer);
}
