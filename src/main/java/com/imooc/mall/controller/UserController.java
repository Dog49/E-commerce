package com.imooc.mall.controller;



import com.imooc.mall.pojo.User;
import com.imooc.mall.service.IUserService;
import com.imooc.mall.vo.ResponseVo;
import form.UserForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Objects;
import static com.imooc.mall.enums.ResponseEnum.NEED_LOGIN;
import static com.imooc.mall.enums.ResponseEnum.PARAM_ERROR;


@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseVo register(@Valid @RequestBody UserForm userForm, BindingResult bindingResult){
      if (bindingResult.hasErrors()){
            log.error("Register Error,{} {}"
                    , Objects.requireNonNull(bindingResult.getFieldError()).getField()
                    ,bindingResult.getFieldError().getDefaultMessage()
            );
            return ResponseVo.error(PARAM_ERROR, bindingResult);

        }

        User user = new User();
        BeanUtils.copyProperties(userForm, user);

        return userService.register(user);
    }
}
