package com.lsx.crm.workbench.service.impl;

import com.lsx.crm.workbench.domain.TranHistory;
import com.lsx.crm.workbench.mapper.TranHistoryMapper;
import com.lsx.crm.workbench.service.TranHistroyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("tranHistroyService")
public class TranHistroyServiceImpl implements TranHistroyService {
    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Override
    public List<TranHistory> queryTranHistoryForDetailByTranId(String tranId) {
        return tranHistoryMapper.selectTranHistoryForDetailByTranId(tranId);
    }
}
