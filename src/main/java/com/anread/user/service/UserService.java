package com.anread.user.service;

import com.anread.common.dto.UserDto;
import com.anread.common.dto.Result;
import com.anread.common.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    /**
     * 用户注册
     * @param userDto
     * @return
     */
    Result<String> userRegister(UserDto userDto);

    /**
     * 用户登录
     * @param userDto
     * @return
     */
    Result<String> userLogin(UserDto userDto);

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return
     */
    Result<UserVO> getUserInfo(String userId);

    /**
     * 上传头像
     * @param userId 用户ID
     * @param file 头像图片文件
     * @return
     */
    Result uploadAvatar(String userId, MultipartFile file);

     /**
     * 更新用户阅读记录
     * @param userId 用户ID
     * @param duration 阅读时长
     * @return
     */
    Result incrementReadingRecord(String userId, Integer duration);
}
