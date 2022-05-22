package com.lsx.crm.workbench.web.controller;

import com.lsx.crm.commons.constants.Constant;
import com.lsx.crm.commons.domain.ReturnObject;
import com.lsx.crm.commons.utils.DateUtils;
import com.lsx.crm.commons.utils.UUIDUtils;
import com.lsx.crm.settings.domain.DicValue;
import com.lsx.crm.settings.domain.User;
import com.lsx.crm.settings.service.DicValueService;
import com.lsx.crm.settings.service.UserService;
import com.lsx.crm.workbench.domain.AfterSale;
import com.lsx.crm.workbench.domain.AfterSaleRemark;
import com.lsx.crm.workbench.service.AfterSaleRemarkService;
import com.lsx.crm.workbench.service.AfterSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VisitController {
    @Autowired
    private AfterSaleService afterSaleService;
    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private AfterSaleRemarkService afterSaleRemarkService;

    @RequestMapping("workbench/visit/index.do")
    public String index(HttpServletRequest request) {
        List<User> userList = userService.queryAllUser();
        List<DicValue> returnStateList = dicValueService.queryDicValueByTypeCode("returnState");
        List<DicValue> returnPriorityList = dicValueService.queryDicValueByTypeCode("returnPriority");
        //添加到作用域
        request.setAttribute("userList", userList);
        request.setAttribute("returnStateList", returnStateList);
        request.setAttribute("returnPriorityList", returnPriorityList);
        return "workbench/visit/index";
    }

    @RequestMapping("/workbench/visvit/save.do")
    public Object toSave(HttpServletRequest request) {
        List<User> userList = userService.queryAllUser();
        List<DicValue> returnStateList = dicValueService.queryDicValueByTypeCode("returnState");
        List<DicValue> returnPriorityList = dicValueService.queryDicValueByTypeCode("returnPriority");
        //添加到作用域
        request.setAttribute("userList", userList);
        request.setAttribute("returnStateList", returnStateList);
        request.setAttribute("returnPriorityList", returnPriorityList);
        return "workbench/visit/save";
    }

    @RequestMapping("/workbench/visit/queryVisitByConditionForPage.do")
    @ResponseBody
    public Object queryVisitByConditionForPage(String owner, String theme, String endDate, String contactsName, String returnState, String returnPriority, int pageNo, int pageSize) {
        //封装参数map
        Map<String, Object> map = new HashMap<>();
        map.put("owner", owner);
        map.put("theme", theme);
        map.put("endDate", endDate);
        map.put("contactsName", contactsName);
        map.put("returnState", returnState);
        map.put("returnPriority", returnPriority);
        map.put("pageNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);

        //调用Service层
        List<AfterSale> afterSalesList = afterSaleService.queryAfterSalrByConditionForPage(map);
        int totalRows = afterSaleService.queryCountOfAfterSaleByCondition(map);
        //封装结果返回json
        Map<String, Object> retMap = new HashMap<>(map);
        retMap.put("afterSalesList", afterSalesList);
        retMap.put("totalRows", totalRows);
        return retMap;
    }

    @RequestMapping("/workbench/visit/insertAfterSale.do")
    public @ResponseBody
    Object insertAfterSale(AfterSale afterSale, HttpSession session) {
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        afterSale.setId(UUIDUtils.getUUID());
        afterSale.setCreateBy(user.getId());
        afterSale.setCreateTime(DateUtils.formateDateTime(new Date()));
        ReturnObject returnObject = new ReturnObject();
        try {
            afterSaleService.saveCreateAfterSale(afterSale);

                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后.....");
        }
    return returnObject;
    }

    @RequestMapping("workbench/visit/queryAfterSaleForDetail.do")
    public String queryAfterSaleForDetail(String id,HttpServletRequest request){
        AfterSale afterSale = afterSaleService.queryAfterSaleForDetailById(id);
        List<AfterSaleRemark> afterSaleRemarkList =
                afterSaleRemarkService.queryAfterSaleRemarkForDetailByAfterSaleId(id);
        request.setAttribute("afterSaleRemarkList",afterSaleRemarkList);
        request.setAttribute("afterSale",afterSale);
        return "workbench/visit/detail";
    }

    /**
     * 删除功能:根据前端勾选需要删除的条数进行删除
     */
    @RequestMapping("/workbench/visit/deleteAfterSaleByIds.do")
    @ResponseBody
    public Object deleteAfterSaleByIds(String[] id){

        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service方法 删除市场活动
            int ret = afterSaleService.deleteAfterSaleById(id);
            if(ret>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后.........");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后.........");
        }
        return returnObject;
    }


}
