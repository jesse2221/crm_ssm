package com.lsx.crm.workbench.service.impl;

import com.lsx.crm.workbench.domain.Contacts;
import com.lsx.crm.workbench.mapper.ContactsMapper;
import com.lsx.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("contactsServiceImpl")
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    private ContactsMapper contactsMapper;

    @Override
    public int saveCrateContacts(Contacts contacts) {
        return contactsMapper.insertContacts(contacts);
    }

    @Override
    public List<Contacts> queryContactsByConditionForPage(Map<String, Object> map) {
        return contactsMapper.selectContactsByConditionForPage(map);
    }

    @Override
    public int queryCountOfContactsByCondition(Map<String, Object> map) {
        return contactsMapper.selectCountOfContactsByCondition(map);
    }

    @Override
    public Contacts queryContactsForDetail(String id) {
        return contactsMapper.selectContactsForDetailById(id);
    }

    @Override
    public int deleteContactsById(String[] ids) {
        return contactsMapper.deleteContactsByIds(ids);
    }

    @Override
    public Contacts queryContactsById(String id) {
        return contactsMapper.selectContactsById(id);
    }
}
