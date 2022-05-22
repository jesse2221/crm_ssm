package com.lsx.crm.workbench.service;

import com.lsx.crm.workbench.domain.ContactsRemark;

import java.util.List;

public interface ContactsRemarkService {
    List<ContactsRemark> queryContactsRemarkForDetailByContactsId(String contactsId);
}
