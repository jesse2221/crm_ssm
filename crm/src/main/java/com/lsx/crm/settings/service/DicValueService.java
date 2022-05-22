package com.lsx.crm.settings.service;

import com.lsx.crm.settings.domain.DicValue;

import java.util.List;


public interface DicValueService {

    List<DicValue> queryDicValueByTypeCode(String typeCode);
}
