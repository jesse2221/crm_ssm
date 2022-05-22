package com.lsx.crm.workbench.web.controller;

import com.lsx.crm.commons.constants.Constant;
import com.lsx.crm.commons.domain.ReturnObject;
import com.lsx.crm.commons.utils.DateUtils;
import com.lsx.crm.settings.domain.DicValue;
import com.lsx.crm.settings.domain.User;
import com.lsx.crm.settings.service.DicValueService;
import com.lsx.crm.settings.service.UserService;
import com.lsx.crm.workbench.domain.*;
import com.lsx.crm.workbench.service.CustomerService;
import com.lsx.crm.workbench.service.TranHistroyService;
import com.lsx.crm.workbench.service.TranRemarkService;
import com.lsx.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class TranController {
    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TranService tranService;
    @Autowired
    private TranRemarkService tranRemarkService;
    @Autowired
    private TranHistroyService tranHistroyService;

    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request){
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList=dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");
        request.setAttribute("stageList",stageList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/toSave.do")
    public String toSave(HttpServletRequest request){
        List<User> userList = userService.queryAllUser();
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList=dicValueService.queryDicValueByTypeCode("transactionType");
        System.out.println(transactionTypeList);
        List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");
        request.setAttribute("userList",userList);
        request.setAttribute("stageList",stageList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        return "workbench/transaction/save";
    }


    @RequestMapping("/workbench/transaction/getPossibilityByStage.do")
    public @ResponseBody Object getpossibilityByStage(String stageValue){
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possbility = bundle.getString(stageValue);
        return possbility;
    }

    @RequestMapping("/workbench/transaction/queryCustomerNameByName.do")
    public @ResponseBody Object queryCustomerNameByName(String customerName){
        List<String> nameList = customerService.queryCustomerNameByName(customerName);
        return nameList;
    }

    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    public @ResponseBody Object saveCreateTran(@RequestParam Map<String,Object> map, HttpSession session){
        map.put(Constant.SESSION_USER,session.getAttribute(Constant.SESSION_USER));
        ReturnObject returnObject = new ReturnObject();
        try{
            tranService.saveCreateTran(map);
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试......");
        }
        return returnObject;
    }


    @RequestMapping("/workbench/transaction/detailTran.do")
    public String detailTran(String id,HttpServletRequest request){
        Tran tran = tranService.queryTranForDetailById(id);
        List<TranRemark> tranRemarkList = tranRemarkService.queryTranRemarkForDetailByTranId(id);
        List<TranHistory> tranHistoryList = tranHistroyService.queryTranHistoryForDetailByTranId(id);
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(tran.getStage());
        tran.setPossibility(possibility);
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("tran",tran);
        request.setAttribute("tranRemarkList",tranRemarkList);
        request.setAttribute("tranHistoryList",tranHistoryList);
        request.setAttribute("stageList",stageList);
        return "/workbench/transaction/detail";
    }



    @RequestMapping("/workbench/tran/queryTranByConditionForPage.do")
    @ResponseBody
    public Object queryTranByConditionForPage(String owner,String name,String customerName,String stage,String type,
                                                  String source, String contactsName,int pageNo,int pageSize){
        //封装参数map
        Map<String,Object> map = new HashMap<>();
        map.put("owner",owner);
        map.put("name",name);
        map.put("customerName",customerName);
        map.put("stage",stage);
        map.put("type",type);
        map.put("source",source);
        map.put("contactsName",contactsName);
        map.put("pageNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);

        //调用Service层
        List<Tran> tranList = tranService.queryTranByConditionForPage(map);
        int totalRows = tranService.queryCountOfTranByCondition(map);
        //封装结果返回json
        Map<String,Object> retMap=new HashMap<>(map);
        retMap.put("tranList",tranList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }


    /**
     * 删除功能:根据前端勾选需要删除的条数进行删除
     */
    @RequestMapping("/workbench/tran/deleteTranByIds.do")
    @ResponseBody
    public Object deleteClueByIds(String[] id){

        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service方法 删除市场活动
            int ret = tranService.deleteTranById(id);
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

    @RequestMapping("/workbench/transaction/toEdit.do")
    public String toEdit(String id,HttpServletRequest request){
        Tran tran=tranService.queryTranById(id);
        tran.setId(id);
        request.setAttribute("tran",tran);
        Tran tran2=tranService.queryTranById2(id);
        request.setAttribute("tran2",tran2);
        List<User> userList = userService.queryAllUser();
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList=dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");
        request.setAttribute("userList",userList);
        request.setAttribute("stageList",stageList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        return "/workbench/transaction/edit";
    }

    /**
     * 交易修改功能
     */
    @RequestMapping("/workbench/tran/SaveEditTran.do")
    public @ResponseBody Object SaveEditCustomer(@RequestParam Map<String,Object> map,HttpSession session){
        map.put(Constant.SESSION_USER,session.getAttribute(Constant.SESSION_USER));
        System.out.println(map.get("id"));
        ReturnObject returnObject=new ReturnObject();
        try {
            int ret = tranService.saveEditTran(map);
            if(ret>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后......");
            }
        }catch (Exception e){
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后......");
        }
        return returnObject;
    }











}
