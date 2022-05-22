package com.lsx.crm.workbench.service;

import com.lsx.crm.workbench.domain.Activity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ActivityService {
    int saveCreateActivity(Activity activity);

    List<Activity> queryActivityByConditionForPage(Map<String,Object> map);

    int queryCountOfActivityByCondition(Map<String,Object> map);

    int deleteActivityById(String[] ids);

    Activity queryActivityById(String id);

    //市场活动修改功能
    int saveEditActivity(Activity activity);

    //导出全部市场活动功能
    List<Activity> queryAllActivitys();

    //导出指定市场活动
    List<Activity> queryActivityByIds(String[] ids);

    //批量导入市场活动
    int saveCreateActivityByList(List<Activity> activityList);

    //查询市场活动详细信息
    Activity queryActivityForDetailById(String id);

    List<Activity> queryActivityForDetailByClueId(String clueId);
    //根据name模糊查询市场活动，并且把已经跟clueId关联过的市场活动排除掉
    List<Activity> queryActivityForDetailByNameClueId(Map<String,Object> map);

    List<Activity> queryActivityForDetailByIds(String[] ids);

    List<Activity> queryActivityForConvertByNameClueId(Map<String,Object> map);
}
