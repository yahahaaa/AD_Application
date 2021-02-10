package com.yahaha.ad.controller;

import com.alibaba.fastjson.JSON;
import com.yahaha.ad.exception.AdException;
import com.yahaha.ad.service.IUserService;
import com.yahaha.ad.vo.CreateUserRequest;
import com.yahaha.ad.vo.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther LeeMZ
 * @Date 2021/2/9
 **/
@Slf4j
@RestController
public class UserOPController {

    @Autowired
    private IUserService userService;

    @PostMapping("/create/user")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest request) throws AdException {

        log.info("【创建用户】用户信息为 -> {}", JSON.toJSONString(request));

        return userService.createUser(request);
    }
}
