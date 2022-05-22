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
import com.lsx.crm.workbench.domain.Clue;
import com.lsx.crm.workbench.domain.ClueActivityRelation;
import com.lsx.crm.workbench.domain.ClueRemark;
import com.lsx.crm.workbench.service.ActivityService;
import com.lsx.crm.workbench.service.ClueActivityRelationService;
import com.lsx.crm.workbench.service.ClueRemarkService;
import com.lsx.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ClueActivityRelationService activityRelationService;



    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request){
        //调用service
        List<User> userList=userService.queryAllUser();
        List<DicValue> appellationList=dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueStateList=dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");
        //添加到作用域
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);
        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/saveCreateClue")
    public @ResponseBody Object saveCreateClue(Clue clue, HttpSession session){
        User user= (User) session.getAttribute(Constant.SESSION_USER);
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateBy(user.getId());
        clue.setCreateTime(DateUtils.formateDateTime(new Date()));
        ReturnObject returnObject= new ReturnObject();
        //调用service
        try {
            int ret = clueService.saveCreateClue(clue);
            if(ret>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/clue/queryClueByConditionForPage.do")
    @ResponseBody
    public Object queryClueByConditionForPage(String fullname,String company, String phone,String source,
                                              String owner,String mphone,String state,int pageNo,int pageSize){
        //封装参数map
        Map<String,Object> map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("pageNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);

        //调用Service层
        List<Clue> clueList = clueService.queryClueByConditionForPage(map);
        int totalRows = clueService.queryCountOfClueByCondition(map);
        //封装结果返回json
        Map<String,Object> retMap=new HashMap<>(map);
        retMap.put("clueList",clueList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }


    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id,HttpServletRequest request){
        //调用service
        List<Activity> activityList=activityService.queryActivityForDetailByClueId(id);
        List<ClueRemark> clueRemarkList = clueRemarkService.queryClueRemarkForDetailByClueId(id);
        Clue clue = clueService.queryClueForDetailById(id);
        request.setAttribute("activityList",activityList);
        request.setAttribute("clueRemarkList",clueRemarkList);
        request.setAttribute("clue",clue);

        return "workbench/clue/detail";
    }

    @RequestMapping("/workbench/clue/queryActivityForDetailByNameClueId.do")
    public @ResponseBody Object queryActivityForDetailByNameClueId(String activityName,String clueId){
        Map<String,Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        List<Activity> activityList = activityService.queryActivityForDetailByNameClueId(map);
        return activityList;
    }

    @RequestMapping("/workbench/clue/saveBund.do")
    public @ResponseBody Object savebund(String[] activityId,String clueId){
        ClueActivityRelation car=null;
        List<ClueActivityRelation> relationslist = new ArrayList<>();
        for(String ai:activityId){
            car = new ClueActivityRelation();
            car.setActivityId(ai);
            car.setClueId(clueId);
            car.setId(UUIDUtils.getUUID());
            relationslist.add(car);
        }
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityRelationService.saveCreateClueActivityRelationByList(relationslist);
            if(ret>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                List<Activity> activityList = activityService.queryActivityForDetailByIds(activityId);
                returnObject.setRetData(activityList);
            }else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试.....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试.....");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/clue/saveUnbund.do")
    public @ResponseBody Object saveUnbund(ClueActivityRelation relation){
        ReturnObject returnObject= new ReturnObject();
        try{
        int ret = activityRelationService.deleteClueActivityRelationByClueIdActivityId(relation);
        if(ret>0){
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后......");
        }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后......");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/clue/convert.do")
    public String toconvert(String id,HttpServletRequest request){
        Clue clue = clueService.queryClueForDetailById(id);
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("clue",clue);
        request.setAttribute("stageList",stageList);
        return "workbench/clue/convert";
    }

    @RequestMapping("/workbench/clue/queryActivityForConvertByNameClueId.do")
    public @ResponseBody Object queryActivityForConvertByNameClueId(String activityName,String clueId){
        Map<String,Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        List<Activity> activityList = activityService.queryActivityForConvertByNameClueId(map);
        return activityList;
    }


    @RequestMapping("/workbench/clue/convertClue.do")
    public @ResponseBody Object convertClue(String clueId,String money,String name,String expectedDate,String stage, String activityId,String isCreateTran,HttpSession session){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("clueId",clueId);
        map.put("money",money);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isCreateTran",isCreateTran);
        map.put(Constant.SESSION_USER,session.getAttribute(Constant.SESSION_USER));
        ReturnObject returnObject = new ReturnObject();
        try{
            clueService.saveConvertClue(map);
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后......");
        }
        return returnObject;
    }

    /**
     * 删除功能:根据前端勾选需要删除的条数进行删除
     */
    @RequestMapping("/workbench/clue/deleteClueByIds.do")
    @ResponseBody
    public Object deleteClueByIds(String[] id){

        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service方法 删除市场活动
            int ret = clueService.deleteClueById(id);
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
     * 根据id查询市场活动
     */
    @RequestMapping("/workbench/clue/queryClueById.do")
    public @ResponseBody Object queryActivityById(String id){
        Clue clue=clueService.queryClueById(id);
        return clue;
    }

    /**
     * 线索活动修改功能
     */
    @RequestMapping("/workbench/clue/SaveEditActivity.do")
    public @ResponseBody Object SaveEditActivity(Clue clue,HttpSession session){
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        clue.setEditTime(DateUtils.formateDateTime(new Date()));
        clue.setEditBy(user.getId());
        ReturnObject returnObject=new ReturnObject();
        try {
            int ret = clueService.saveEditClue(clue);
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
