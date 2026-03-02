package com.anread.user.controller;

import com.anread.common.dto.UserDto;
import com.anread.common.vo.UserVO;
import com.anread.user.service.UserService;
import com.anread.common.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户服务接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param userDto
     * @return
     */
    @PostMapping("/register")
    public Result<String> userRegister(@RequestBody UserDto userDto) {
        return userService.userRegister(userDto);
    }

    /**
     * 用户登录
     * @param userDto
     * @return
     */
    @PostMapping("/login")
    public Result<String> userLogin(@RequestBody UserDto userDto) {
        return userService.userLogin(userDto);
    }

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return
     */
    @GetMapping("/info")
    public Result<UserVO> getUserInfo(@RequestHeader("X-User-ID") String userId) {
        return userService.getUserInfo(userId);
    }

    /**
     * 上传用户头像
     * @param userId 用户ID
     * @param file 头像图片文件
     * @return
     */
    @PostMapping("/avatar")
    public Result uploadAvatar(@RequestHeader("X-User-ID") String userId, @RequestParam("file") MultipartFile file) {
        return userService.uploadAvatar(userId, file);
    }

    /**
     * 更新用户阅读记录
     * @param userId 用户ID
     * @param duration 阅读时长
     * @return
     */
    @PutMapping("/reading-record")
    public Result incrementReadingRecord(@RequestParam("userId") String userId, @RequestParam("duration") Integer duration) {
        return userService.incrementReadingRecord(userId, duration);
    }

}
