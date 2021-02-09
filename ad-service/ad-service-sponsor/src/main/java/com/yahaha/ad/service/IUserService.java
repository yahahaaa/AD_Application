package com.yahaha.ad.service;

import com.yahaha.ad.exception.AdException;
import com.yahaha.ad.vo.CreateUserRequest;
import com.yahaha.ad.vo.CreateUserResponse;

public interface IUserService {

    /**
     * 创建用户
     * @param request
     * @return
     * @throws AdException
     */
    CreateUserResponse createUser(CreateUserRequest request) throws AdException;
}
