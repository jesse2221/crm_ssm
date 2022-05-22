package com.lsx.crm.workbench.service;

import com.lsx.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String actviityId);

    int saveCreateActivityRemark(ActivityRemark activityRemark);

    int deleteActivityRemarkById(String id);

    int saveEditActivityRemark(ActivityRemark activityRemark);
}
