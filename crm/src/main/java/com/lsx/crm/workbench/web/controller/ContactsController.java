package com.lsx.crm.workbench.web.controller;

import com.lsx.crm.commons.constants.Constant;
import com.lsx.crm.commons.domain.ReturnObject;
import com.lsx.crm.commons.utils.DateUtils;
import com.lsx.crm.commons.utils.UUIDUtils;
import com.lsx.crm.settings.domain.DicValue;
import com.lsx.crm.settings.domain.User;
import com.lsx.crm.settings.service.DicValueService;
import com.lsx.crm.settings.service.UserService;
import com.lsx.crm.workbench.domain.Activity;
import com.lsx.crm.workbench.domain.Contacts;
import com.lsx.crm.workbench.domain.ContactsRemark;
import com.lsx.crm.workbench.domain.Customer;
import com.lsx.crm.workbench.service.ContactsRemarkService;
import com.lsx.crm.workbench.service.ContactsService;
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
public class ContactsController {
    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private ContactsRemarkService remarkService;

    @RequestMapping("/workbench/contacts/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userService.queryAllUser();
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        request.setAttribute("userList",userList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("appellationList",appellationList);
        return "workbench/contacts/index";
    }

    @RequestMapping("/workbench/contacts/saveCreateContacts.do")
    public @ResponseBody Object saveCreateContacts(Contacts contacts, HttpSession session){
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        contacts.setId(UUIDUtils.getUUID());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formateDateTime(new Date()));
        ReturnObject returnObject=new ReturnObject();
        try{
            int ret = contactsService.saveCrateContacts(contacts);
            if(ret>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后.....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后.....");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/contacts/queryContactsByConditionForPage.do")
    @ResponseBody
    public Object queryContactsByConditionForPage(String owner,String fullname,String customerName,String source,
                                                  String birthday, int pageNo,int pageSize){
        //封装参数map
        Map<String,Object> map = new HashMap<>();
        map.put("owner",owner);
        map.put("fullname",fullname);
        map.put("customerName",customerName);
        map.put("source",source);
        map.put("birthday",birthday);
        map.put("pageNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);

        //调用Service层
        List<Contacts> contactsList = contactsService.queryContactsByConditionForPage(map);
        int totalRows = contactsService.queryCountOfContactsByCondition(map);
        //封装结果返回json
        Map<String,Object> retMap=new HashMap<>(map);
        retMap.put("contactsList",contactsList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }

    @RequestMapping("workbench/contacts/queryContactsForDetail.do")
    public String queryContactsForDetail(String id,HttpServletRequest request){
        Contacts contacts=contactsService.queryContactsForDetail(id);
        List<ContactsRemark> contactsRemarkList = remarkService.queryContactsRemarkForDetailByContactsId(id);
        request.setAttribute("contacts",contacts);
        request.setAttribute("remark",contactsRemarkList);
        return "workbench/contacts/detail";
    }

    /**
     * 删除功能:根据前端勾选需要删除的条数进行删除
     */
    @RequestMapping("/workbench/contacts/deleteContactsByIds.do")
    @ResponseBody
    public Object deleteClueByIds(String[] id){

        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service方法 删除市场活动
            int ret = contactsService.deleteContactsById(id);
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

    /**
     * 根据id查询客户
     */
    @RequestMapping("/workbench/contacts/queryContactsById.do")
    public @ResponseBody Object queryActivityById(String id){
        Contacts contacts = contactsService.queryContactsById(id);
        return contacts;
    }
}
