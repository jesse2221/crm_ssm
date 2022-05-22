package com.lsx.crm.workbench.service;

import com.lsx.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistroyService {
    List<TranHistory> queryTranHistoryForDetailByTranId(String tranId);
}
