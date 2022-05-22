package com.lsx.crm.settings.service.impl;

import com.lsx.crm.settings.domain.DicValue;
import com.lsx.crm.settings.mapper.DicValueMapper;
import com.lsx.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("dicValueService")
public class DicValueServiceimpl implements DicValueService {
    @Autowired
    private DicValueMapper dicValueMapper;

    @Override
    public List<DicValue> queryDicValueByTypeCode(String typeCode) {
        return dicValueMapper.selectDicValueByTypeCode(typeCode);
    }
}
