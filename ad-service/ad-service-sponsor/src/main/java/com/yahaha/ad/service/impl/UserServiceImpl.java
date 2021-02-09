package com.yahaha.ad.service.impl;

import com.yahaha.ad.constant.Constants;
import com.yahaha.ad.dao.AdUserRepository;
import com.yahaha.ad.entity.AdUser;
import com.yahaha.ad.exception.AdException;
import com.yahaha.ad.service.IUserService;
import com.yahaha.ad.utils.CommonUtils;
import com.yahaha.ad.vo.CreateUserRequest;
import com.yahaha.ad.vo.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther LeeMZ
 * @Date 2021/2/7
 **/
@Slf4j
@Service
@SuppressWarnings("all")
public class UserServiceImpl implements IUserService {

    @Autowired
    private AdUserRepository userRepository;

    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) throws AdException {

        //验证用户名是否为空
        if (!request.validate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        //判断是否该用户名已存在
        AdUser oldUser = userRepository.findByUsername(request.getUsername());
        if (oldUser != null){
            throw new AdException(Constants.ErrorMsg.SAME_NAME_ERROR);
        }

        //用户入库
        AdUser newUser = userRepository.save(new AdUser(request.getUsername(), CommonUtils.md5(request.getUsername())));

        return new CreateUserResponse(newUser.getId(),newUser.getUsername(),newUser.getToken(),
                newUser.getCreateTime(),newUser.getUpdateTime());
    }
}
