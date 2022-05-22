package com.lsx.crm.workbench.web.controller;

import com.lsx.crm.commons.constants.Constant;
import com.lsx.crm.commons.domain.ReturnObject;
import com.lsx.crm.commons.utils.DateUtils;
import com.lsx.crm.commons.utils.HSSFUtils;
import com.lsx.crm.commons.utils.UUIDUtils;
import com.lsx.crm.settings.domain.User;
import com.lsx.crm.settings.service.UserService;
import com.lsx.crm.workbench.domain.Activity;
import com.lsx.crm.workbench.domain.ActivityRemark;
import com.lsx.crm.workbench.service.ActivityRemarkService;
import com.lsx.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userService.queryAllUser();
        request.setAttribute("userList",userList);
        return "workbench/activity/index";
    }
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session){
        User user= (User) session.getAttribute(Constant.SESSION_USER);
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateBy(user.getId());
        activity.setCreateTime(DateUtils.formateDateTime(new Date()));
        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service
            int ret = activityService.saveCreateActivity(activity);
            if(ret>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙...请稍后");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙...请稍后");
        }
        return returnObject;
    }


    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name,String owner, String startDate,String endDate,
                                                  int pageNo,int pageSize){
        //封装参数map
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("pageNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);

        //调用Service层
        List<Activity> activitiesList = activityService.queryActivityByConditionForPage(map);
        int totalRows = activityService.queryCountOfActivityByCondition(map);
        //封装结果返回json
        Map<String,Object> retMap=new HashMap<>(map);
        retMap.put("activitiesList",activitiesList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }

    /**
     * 删除功能:根据前端勾选需要删除的条数进行删除
     */
    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id){

        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service方法 删除市场活动
            int ret = activityService.deleteActivityById(id);
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
    @RequestMapping("/workbench/activity/queryActivityById.do")
    public @ResponseBody Object queryActivityById(String id){
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }

    /**
     * 市场活动修改功能
     */
    @RequestMapping("/workbench/activity/SaveEditActivity.do")
    public @ResponseBody Object SaveEditActivity(Activity activity,HttpSession session){
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        activity.setEditTime(DateUtils.formateDateTime(new Date()));
        activity.setEditBy(user.getId());
        ReturnObject returnObject=new ReturnObject();
        try {
            int ret = activityService.saveEditActivity(activity);
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

    /**
     * 导出市场活动
     */
    @RequestMapping("/workbench/activity/queryAllActivitys.do")
    public void queryAllActivitys (HttpServletResponse response) throws Exception {
        //调用service
        List<Activity> activityList = activityService.queryAllActivitys();
        ////创建excel文件，并且把activityList写入到excel文件中
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet =wb.createSheet("市场活动列表");
        HSSFRow row =sheet.createRow(0);
        HSSFCell cell=row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("活动名称");
        cell = row.createCell(3);
        cell.setCellValue("开始时间");
        cell = row.createCell(4);
        cell.setCellValue("结束时间");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("活动描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改人");

        if(activityList!=null && activityList.size()>0){
            Activity activities=null;
            //遍历activityList,创建HSSFRow对象，生成所有的数据行
            for(int i=0;i<activityList.size();i++){
                activities=activityList.get(i);
                //每遍历出一个activiy，生成一行
                row=sheet.createRow(i+1);
                //每一行创建11列，每一列的数据从activity中获取
                cell=row.createCell(0);
                cell.setCellValue(activities.getId());
                cell = row.createCell(1);
                cell.setCellValue(activities.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activities.getName());
                cell = row.createCell(3);
                cell.setCellValue(activities.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activities.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activities.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activities.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activities.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activities.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activities.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activities.getEditBy());
            }
        }
        //根据workbook对象生成excel文件
        // OutputStream os = new FileOutputStream("D:\\卷基地\\CRM项目（SSM框架版）\\项目资料\\serverDir\\activityList");
        // wb.write(os);
        //关闭资源
        // os.close();
        // wb.close();

        //把生成的excel下载到用户的客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=activityList.xls");
        OutputStream out=response.getOutputStream();
        // InputStream is=new FileInputStream("D:\\卷基地\\CRM项目（SSM框架版）\\项目资料\\serverDir\\activityList");
        // byte[] buff = new byte[256];
        // int length=0;
        // while ((length=is.read(buff))!=-1){
        //     out.write(buff,0,length);
        // }
        // is.close();
        wb.write(out);
        out.flush();
   }

    /**
     * 导出指定的市场活动
     */
    @RequestMapping("/workbench/activity/queryActivityByIds.do")
   public void queryActivityByIds(String[] id,HttpServletResponse response) throws Exception {
        System.out.println(id);
        List<Activity> activityList = activityService.queryActivityByIds(id);
        //创建workbook
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("Id");
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("活动名称");
        cell=row.createCell(3);
        cell.setCellValue("开始时间");
        cell=row.createCell(4);
        cell.setCellValue("结束时间");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("活动描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改人");

        if(activityList!=null && activityList.size()>0){
            Activity activity=null;
            for(int i=0;i<activityList.size();i++){
                activity = activityList.get(i);
                row=sheet.createRow(i+1);
                cell=row.createCell(0);
                cell.setCellValue(activity.getId());
                cell=row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=activityList.xls");
        OutputStream out=response.getOutputStream();
        wb.write(out);
        out.flush();
    }


    @RequestMapping("/workbench/activity/importActivity.do")
    public @ResponseBody Object importActivity(MultipartFile activityFile,String userName,HttpSession session){
        System.out.println("userName="+userName);
        User user=(User) session.getAttribute(Constant.SESSION_USER);
        ReturnObject returnObject=new ReturnObject();
        try {
            //把excel文件写到磁盘目录中
            String originalFilename = activityFile.getOriginalFilename();
            File file = new File("D:\\crm\\", originalFilename);//路径必须手动创建好，文件如果不存在，会自动创建
            activityFile.transferTo(file);

            //解析excel文件，获取文件中的数据，并且封装成activityList
            // 根据excel文件生成HSSFWorkbook对象，封装了excel文件的所有信息
            InputStream is=new FileInputStream("D:\\crm\\"+originalFilename);

            // InputStream is=activityFile.getInputStream();
            HSSFWorkbook wb=new HSSFWorkbook(is);
            //根据wb获取HSSFSheet对象，封装了一页的所有信息
            HSSFSheet sheet=wb.getSheetAt(0);//页的下标，下标从0开始，依次增加
            //根据sheet获取HSSFRow对象，封装了一行的所有信息
            HSSFRow row=null;
            HSSFCell cell=null;
            Activity activity=null;
            List<Activity> activityList=new ArrayList<>();
            for(int i=1;i<=sheet.getLastRowNum();i++) {//sheet.getLastRowNum()：最后一行的下标
                row=sheet.getRow(i);//行的下标，下标从0开始，依次增加
                activity=new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtils.formateDateTime(new Date()));
                activity.setCreateBy(user.getId());

                for(int j=0;j<row.getLastCellNum();j++) {//row.getLastCellNum():最后一列的下标+1
                    //根据row获取HSSFCell对象，封装了一列的所有信息
                    cell=row.getCell(j);//列的下标，下标从0开始，依次增加

                    //获取列中的数据
                    String cellValue=HSSFUtils.getCellValueForStr(cell);
                    if(j==0){
                        activity.setName(cellValue);
                    }else if(j==1){
                        activity.setStartDate(cellValue);
                    }else if(j==2){
                        activity.setEndDate(cellValue);
                    }else if(j==3){
                        activity.setCost(cellValue);
                    }else if(j==4){
                        activity.setDescription(cellValue);
                    }
                }

                //每一行中所有列都封装完成之后，把activity保存到list中
                activityList.add(activity);
            }

            //调用service层方法，保存市场活动
            int ret=activityService.saveCreateActivityByList(activityList);

            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(ret);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }

        return returnObject;
    }














}
