package com.lsx.crm.workbench.mapper;

import com.lsx.crm.workbench.domain.AfterSale;

import java.util.List;
import java.util.Map;

public interface AfterSaleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_after_sale
     *
     * @mbggenerated Thu May 05 22:08:40 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_after_sale
     *
     * @mbggenerated Thu May 05 22:08:40 CST 2022
     */
    int insert(AfterSale record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_after_sale
     *
     * @mbggenerated Thu May 05 22:08:40 CST 2022
     */
    int insertSelective(AfterSale record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_after_sale
     *
     * @mbggenerated Thu May 05 22:08:40 CST 2022
     */
    AfterSale selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_after_sale
     *
     * @mbggenerated Thu May 05 22:08:40 CST 2022
     */
    int updateByPrimaryKeySelective(AfterSale record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_after_sale
     *
     * @mbggenerated Thu May 05 22:08:40 CST 2022
     */
    int updateByPrimaryKey(AfterSale record);

    /**
     * 根据条件查询所有的售后回访
     */
    List<AfterSale> selectAfterSaleByConditionForPage(Map<String,Object> map);

    /**
     * 根据条件查询符合条件的所有条数
     */
    int selectCountOfAfterSaleByCondition(Map<String,Object> map);

    void insertAfterSale(AfterSale afterSale);

    AfterSale selectAfterSaleForDetailById(String id);

    /**
     * 根据id删除符合条件的
     * @param ids
     * @return
     */
    int deleteAfterSaleByIds(String[] ids);
}