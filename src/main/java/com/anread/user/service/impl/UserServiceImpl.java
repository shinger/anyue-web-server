package com.anread.user.service.impl;

import com.anread.common.dto.UserDto;
import com.anread.common.enums.StateEnum;
import com.anread.common.vo.UserVO;
import com.anread.feign.IFileClient;
import com.anread.user.mapper.ReadingRecordMapper;
import com.anread.user.mapper.UserMapper;
import com.anread.user.service.UserService;
import com.anread.common.dto.Result;
import com.anread.common.entity.User;
import com.anread.common.utils.CommonUtil;
import com.anread.common.utils.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ReadingRecordMapper readingRecordMapper;

    @Autowired
    private IFileClient fileClient;

    @Override
    public Result<String> userRegister(UserDto userDto) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, userDto.getEmail()));
        if (user != null) {
            return Result.error("该邮箱已注册");
        }

        String userId = CommonUtil.generateRandomID(8);
        String token = JWTUtil.getToken(userId, userDto.getPassword());
        userMapper.insert(User.builder()
                .id(userId)
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .readingDuration(0)
                .followers(0)
                .likesCount(0)
                .token(token)
                .build());

        return Result.<String>success().data(token);
    }

    @Override
    public Result<String> userLogin(UserDto userDto) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, userDto.getEmail()));
        if (user == null) {
            return Result.error("该邮箱未注册");
        }
        if (!user.getPassword().equals(userDto.getPassword())) {
            return Result.error("密码错误");
        }

        String token = JWTUtil.getToken(user.getId(), userDto.getPassword());

        return Result.<String>success().data(token);
    }

    @Override
    public Result<UserVO> getUserInfo(String userId) {
        User user = userMapper.selectById(userId);

        if (user == null) {
            log.error("User login failed, user does not exist. [userId:{}]", userId);
            return Result.error("用户不存在");
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserId(userId);
        log.info("User login success. [userId:{}]", user.getId());
        return Result.<UserVO>success().data(userVO);
    }

    @Override
    public Result uploadAvatar(String userId, MultipartFile file) {
        Result<String> result = fileClient.uploadCommonFile(file);

        if (!result.getCode().equals("200")) {
            return Result.error(StateEnum.UPLOAD_FAILED);
        }


        // 更新数据库中的用户头像
        try {
            int i = userMapper.update(new LambdaUpdateWrapper<User>()
                        .eq(User::getId, userId)
                        .set(User::getAvatar, result.getData()));
            if (i != 1) {
                return Result.error(StateEnum.UPLOAD_FAILED);
            }
        } catch (Exception e) {
            // 数据库更新失败
            log.error("【User - Upload Avatar 】 用户ID：{} 信息：{}", userId, e.getMessage());
            // 删除Minio中的文件
            Result deleteResult = fileClient.deleteCommonFile(result.getData());
            if (!deleteResult.isSuccess()) {
                return Result.error(StateEnum.UPLOAD_FAILED);
            }
            return Result.error(StateEnum.UPLOAD_FAILED);
        }

        return Result.success().data(result.getData());
    }

    @Override
    public Result incrementReadingRecord(String userId, Integer duration) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }
        if (duration == null) {
            return Result.error("阅读时长不能为空");
        }

        int update = userMapper.update(new LambdaUpdateWrapper<User>()
                .eq(User::getId, userId)
                .setIncrBy(User::getReadingDuration, duration));
        if (update != 1) {
            return Result.error("更新阅读记录失败");
        }
        return Result.success();
    }
}
