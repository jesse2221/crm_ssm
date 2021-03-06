package com.lsx.crm.workbench.web.controller;

import com.lsx.crm.commons.constants.Constant;
import com.lsx.crm.commons.domain.ReturnObject;
import com.lsx.crm.commons.utils.DateUtils;
import com.lsx.crm.commons.utils.UUIDUtils;
import com.lsx.crm.settings.domain.User;
import com.lsx.crm.settings.service.UserService;
import com.lsx.crm.workbench.domain.*;
import com.lsx.crm.workbench.service.CustomerRemarkService;
import com.lsx.crm.workbench.service.CustomerService;
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
public class CustomerController {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRemarkService customerRemarkService;

    @RequestMapping("/workbench/customer/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userService.queryAllUser();
        request.setAttribute("userList",userList);
        return "workbench/customer/index";
    }

    @RequestMapping("/workbench/customer/saveCreateCustomer.do")
    public @ResponseBody Object saveCreateCustomer(Customer customer, HttpSession session){
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        customer.setId(UUIDUtils.getUUID());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formateDateTime(new Date()));

        ReturnObject returnObject = new ReturnObject();
        try{
            int ret=customerService.saveCreateCustomer(customer);
            if(ret>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("???????????????????????????........");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("???????????????????????????........");
        }

        return returnObject;
    }


    @RequestMapping("/workbench/customer/queryCustomerByConditionForPage.do")
    @ResponseBody
    public Object queryCustomerByConditionForPage(String name,String owner, String phone,String website, int pageNo,int pageSize){
        //????????????map
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("phone",phone);
        map.put("website",website);
        map.put("pageNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);

        //??????Service???
        List<Customer> customerList = customerService.queryCustomerByConditionForPage(map);
        int totalRows = customerService.queryCountOfClueByCondition(map);
        //??????????????????json
        Map<String,Object> retMap=new HashMap<>(map);
        retMap.put("customerList",customerList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }


    @RequestMapping("/workbench/customer/queryCustomerForDetail.do")
    public String queryCustomerForDetail(String id, HttpServletRequest request){
        //??????service
        Customer customer=customerService.queryCustomerForDetailById(id);
        List<CustomerRemark> customerRemarkList = customerRemarkService.queryCustomerRemarkForDatailByCustomerId(id);
        System.out.println(customer);
        request.setAttribute("customer",customer);
        request.setAttribute("customerRemarkList",customerRemarkList);
        return "workbench/customer/detail";
    }


    /**
     * ????????????:???????????????????????????????????????????????????
     */
    @RequestMapping("/workbench/customer/deleteCustomerByIds.do")
    @ResponseBody
    public Object deleteClueByIds(String[] id){

        ReturnObject returnObject = new ReturnObject();
        try {
            //??????service?????? ??????????????????
            int ret = customerService.deleteCustomerById(id);
            if(ret>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("?????????????????????.........");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("?????????????????????.........");
        }
        return returnObject;
    }

    /**
     * ??????id????????????
     */
    @RequestMapping("/workbench/customer/queryCustomerById.do")
    public @ResponseBody Object queryActivityById(String id){
        Customer customer=customerService.queryCustomerById(id);
        return customer;
    }

    /**
     * ??????????????????
     */
    @RequestMapping("/workbench/customer/SaveEditCustomer.do")
    public @ResponseBody Object SaveEditCustomer(Customer customer,HttpSession session){
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        customer.setEditTime(DateUtils.formateDateTime(new Date()));
        customer.setEditBy(user.getId());
        ReturnObject returnObject=new ReturnObject();
        try {
            int ret = customerService.saveEditCustomer(customer);
            if(ret>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("?????????????????????......");
            }
        }catch (Exception e){
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("?????????????????????......");
        }
        return returnObject;
    }
}
