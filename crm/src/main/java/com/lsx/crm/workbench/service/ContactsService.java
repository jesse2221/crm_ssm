package com.lsx.crm.workbench.service;

import com.lsx.crm.workbench.domain.Clue;
import com.lsx.crm.workbench.domain.Contacts;
import com.lsx.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface ContactsService {
    int saveCrateContacts(Contacts contacts);

    List<Contacts> queryContactsByConditionForPage(Map<String,Object> map);

    int queryCountOfContactsByCondition(Map<String,Object> map);

    Contacts  queryContactsForDetail(String id);

    int deleteContactsById(String[] ids);

    Contacts queryContactsById(String id);
}
