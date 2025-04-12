package com.anyue.server.controller;

import com.anyue.server.service.UserService;
import com.anyue.server.vo.UserVO;
import com.anyue.common.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    private Result userRegister(@RequestBody UserVO userVO) {
        return userService.userRegister(userVO);
    }

    @PostMapping("/login")
    private Result userLogin(@RequestBody UserVO userVO) {
        return userService.userLogin(userVO);
    }

    @GetMapping("/info")
    private Result getUserInfo() {
        return userService.getUserInfo();
    }

}
