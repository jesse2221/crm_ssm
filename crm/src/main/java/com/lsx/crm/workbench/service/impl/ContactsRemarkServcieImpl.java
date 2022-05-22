package com.lsx.crm.workbench.service.impl;

import com.lsx.crm.workbench.domain.ContactsRemark;
import com.lsx.crm.workbench.mapper.ContactsRemarkMapper;
import com.lsx.crm.workbench.service.ContactsRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("contactsRemarkService")
public class ContactsRemarkServcieImpl implements ContactsRemarkService {
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Override
    public List<ContactsRemark> queryContactsRemarkForDetailByContactsId(String contactsId) {
        return contactsRemarkMapper.selectContactsRemarkForDetailByContactsId(contactsId);
    }
}
