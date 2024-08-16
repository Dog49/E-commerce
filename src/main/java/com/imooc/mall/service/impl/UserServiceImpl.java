package com.imooc.mall.service.impl;

import com.imooc.mall.dao.UserMapper;
import com.imooc.mall.enums.RoleEnum;
import com.imooc.mall.pojo.User;
import com.imooc.mall.service.IUserService;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

import static com.imooc.mall.enums.ResponseEnum.*;

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
    public ResponseVo<User> register(User user) {
        error();

        //username should be unique
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if(countByUsername > 0){
            return ResponseVo.error(USERNAME_EXIST);
        }
        //email should be unique
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if(countByEmail > 0){
            return ResponseVo.error(EMAIL_EXIST);
        }

        //MD5 Algorithm(Springboot)
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        user.setRole(RoleEnum.CUSTOMER.getCode());
        //save to database, if resultCount == 0, means failed
        int resultCount = userMapper.insertSelective(user);
        if(resultCount == 0){
            return ResponseVo.error(ERROR);
        }

        return ResponseVo.success();
    }

    /*
     * Login
     *
     * */
    @Override
    public ResponseVo<User> login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if(user == null){
            //user does not exist (unsafe), do not let user know the exact reason
            return ResponseVo.error(USERNAME_OR_PASSWORD_ERROR);
        }
        if(!user.getPassword().equalsIgnoreCase(
                DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)))) {
            //password is wrong
            return ResponseVo.error(USERNAME_OR_PASSWORD_ERROR);
        }

        user.setPassword(""); //do not return password
        return ResponseVo.success(user);
    }



    private void error() {
        throw new RuntimeException("Unknown Error");
    }
}
