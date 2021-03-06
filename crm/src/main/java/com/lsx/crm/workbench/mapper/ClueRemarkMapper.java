package com.lsx.crm.workbench.mapper;

import com.lsx.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Tue Apr 05 23:39:24 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Tue Apr 05 23:39:24 CST 2022
     */
    int insert(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Tue Apr 05 23:39:24 CST 2022
     */
    int insertSelective(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Tue Apr 05 23:39:24 CST 2022
     */
    ClueRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Tue Apr 05 23:39:24 CST 2022
     */
    int updateByPrimaryKeySelective(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Tue Apr 05 23:39:24 CST 2022
     */
    int updateByPrimaryKey(ClueRemark record);

    /**
     * 根据clueId查询线索备注表所关联的所有备注
     * @param clueId
     * @return
     */
    List<ClueRemark> selectClueRemarkForDetailByClueId(String clueId);

    /**
     * 根据clueId查询该线索下所有的备注信息
     * @param id
     * @return
     */
    List<ClueRemark> selectClueRemarkById(String id);

    /**
     * 根据clueId删除线索下的所有备注
     * @param clueId
     * @return
     */
    int deleteClueRemarkByClueId(String clueId);
}