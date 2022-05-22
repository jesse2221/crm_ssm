package com.lsx.crm.settings.service.impl;

import com.lsx.crm.settings.domain.User;
import com.lsx.crm.settings.mapper.UserMapper;
import com.lsx.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndPwd(Map<String,Object> map) {
        return userMapper.selectUserByLoginActAndPwd(map);
    }

    @Override
    public List<User> queryAllUser() {
        return userMapper.selectAllUser();
    }

    @Override
    public User queryUserDetailById(String userId) {
        return userMapper.selectUserDetailById(userId);
    }
}
