package com.lsx.crm.workbench.mapper;

import com.lsx.crm.workbench.domain.Clue;
import com.lsx.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Mon Apr 11 00:03:26 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Mon Apr 11 00:03:26 CST 2022
     */
    int insert(Customer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Mon Apr 11 00:03:26 CST 2022
     */
    int insertSelective(Customer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Mon Apr 11 00:03:26 CST 2022
     */
    Customer selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Mon Apr 11 00:03:26 CST 2022
     */
    int updateByPrimaryKeySelective(Customer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Mon Apr 11 00:03:26 CST 2022
     */
    int updateByPrimaryKey(Customer record);

    int insertCustomer(Customer customer);

    List<String> selectCustomerNameByName(String name);

    Customer queryCustomerByName(String name);

    /**
     * 根据条件查询所有的线索列表
     */
    List<Customer> selectCustomerByConditionForPage(Map<String,Object> map);

    /**
     * 根据条件查询符合条件的所有条数
     */
    int selectCountOfCustomerByCondition(Map<String,Object> map);

    Customer selectCustomerForDetailById(String id);

    /**
     * 根据id删除符合条件的
     * @param ids
     * @return
     */
    int deleteCustomerByIds(String[] ids);

    /**
     *  通过Id查询线索详细信息
     * @param id
     * @return
     */
    Customer selectCustomerById(String id);

    /**
     * 市场活动修改功能
     */
    int updateCustomer(Customer customer);
}