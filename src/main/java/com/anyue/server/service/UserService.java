package com.anyue.server.service;

import com.anyue.server.vo.UserVO;
import com.anyue.common.dto.Result;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    Result userRegister(UserVO userVO);

    Result userLogin(UserVO userVO);

    Result getUserInfo();
}
