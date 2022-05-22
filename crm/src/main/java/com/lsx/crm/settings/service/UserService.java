package com.lsx.crm.settings.service;

import com.lsx.crm.settings.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    User queryUserByLoginActAndPwd(Map<String,Object> map);

    List<User> queryAllUser();

    User queryUserDetailById(String userId);
}
