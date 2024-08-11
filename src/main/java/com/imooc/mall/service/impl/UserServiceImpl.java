package com.imooc.mall.service.impl;

import com.imooc.mall.dao.UserMapper;
import com.imooc.mall.pojo.User;
import com.imooc.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /*
    * Register
    *
    * @param user
    * */
    @Override
    public void register(User user) {
        //username should be unique
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if(countByUsername > 0){
            throw new RuntimeException("username already exists");
        }
        //email should be unique
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if(countByEmail > 0){
            throw new RuntimeException("email already exists");
        }

        //MD5 Algorithm(Springboot)
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));

        //save to database, if resultCount == 0, means failed
        int resultCount = userMapper.insertSelective(user);
        if(resultCount == 0){
            throw new RuntimeException("register failed");
        }



    }

    /*
    * Login
    *
    * */
}
