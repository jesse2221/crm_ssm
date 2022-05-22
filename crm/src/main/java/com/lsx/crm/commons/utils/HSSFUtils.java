package com.lsx.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * 关于excel文件操作的工具类
 */
public class HSSFUtils {
    /**
     * 从指定的HSSFCell对象中获取列的值
     * @return
     */
    public static String getCellValueForStr(HSSFCell cell){
        String ret="";
        if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
            ret=cell.getStringCellValue();
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
            //数值类型
            ret=cell.getNumericCellValue()+"";
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
            ret=cell.getBooleanCellValue()+"";
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA){
            //formula表示公式类型，
            ret=cell.getCellFormula();
        }else{
            ret="";
        }

        return ret;
    }
}
