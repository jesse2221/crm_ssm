package com.lsx.crm.workbench.service;

import com.lsx.crm.workbench.domain.Activity;
import com.lsx.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    int saveCreateClue(Clue clue);

    Clue queryClueForDetailById(String id);

    void saveConvertClue(Map<String,Object> map);

    List<Clue> queryClueByConditionForPage(Map<String,Object> map);

    int queryCountOfClueByCondition(Map<String,Object> map);

    int deleteClueById(String[] ids);

    Clue queryClueById(String id);

    //市场活动修改功能
    int saveEditClue(Clue clue);
}
