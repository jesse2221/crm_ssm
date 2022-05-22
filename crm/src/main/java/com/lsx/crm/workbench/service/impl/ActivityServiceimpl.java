package com.lsx.crm.workbench.service.impl;

import com.lsx.crm.workbench.domain.Activity;
import com.lsx.crm.workbench.mapper.ActivityMapper;
import com.lsx.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("activityService")
public class ActivityServiceimpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public int saveCreateActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }

    /**
     * 查询符合条件的Activity对象列表
     */
    @Override
    public List<Activity> queryActivityByConditionForPage(Map<String, Object> map) {
        return activityMapper.selectActivityByConditionForPage(map);
    }

    /**
     * 查询符合条件的总条数
     */
    @Override
    public int queryCountOfActivityByCondition(Map<String, Object> map) {
        return activityMapper.queryCountOfActivityByCondition(map);
    }

    /**
     * 删除前端传来id需要删除的条数
     */
    @Override
    public int deleteActivityById(String[] ids) {
        return activityMapper.deleteActivityByIds(ids);
    }

    /**
     * 根据id查询市场活动
     */
    @Override
    public Activity queryActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }

    /**
     * 市场活动修改功能
     */
    @Override
    public int saveEditActivity(Activity activity) {
        return activityMapper.updateActivity(activity);
    }

    /**
     * 导出市场活动功能
     */
    @Override
    public List<Activity> queryAllActivitys() {
        return activityMapper.selectAllActivitys();
    }

    /**
     * 导出指定的市场活动
     */
    @Override
    public List<Activity> queryActivityByIds(String[] ids) {
        return activityMapper.selectActivityByIds(ids);
    }

    /**
     *  批量导入市场活动
     */
    @Override
    public int saveCreateActivityByList(List<Activity> activityList) {
        return activityMapper.insertActivityByList(activityList);
    }

    /**
     * 根据id查询市场活动详细信息
     */
    @Override
    public Activity queryActivityForDetailById(String id) {
        return activityMapper.selectActivityForDetailById(id);
    }

    /**
     * 根据线索id查询与该线索关联的市场活动
     */
    @Override
    public List<Activity> queryActivityForDetailByClueId(String clueId) {
        return activityMapper.selectActivityForDetailByClueId(clueId);
    }

    /**
     * 根据name模糊查询市场活动，并且把已经跟clueId关联过的市场活动排除掉
     */
    @Override
    public List<Activity> queryActivityForDetailByNameClueId(Map<String, Object> map) {
        return activityMapper.selectActivityForDetailByNameClueId(map);
    }

    @Override
    public List<Activity> queryActivityForDetailByIds(String[] ids) {
        return activityMapper.selectActivityForDetailByIds(ids);
    }

    @Override
    public List<Activity> queryActivityForConvertByNameClueId(Map<String, Object> map) {
        return activityMapper.selectActivityForConvertByNameClueId(map);
    }
}
