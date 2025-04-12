package com.anyue.server.service.impl;

import com.anyue.server.dto.UserDto;
import com.anyue.server.mapper.UserMapper;
import com.anyue.server.service.UserService;
import com.anyue.server.utils.ThreadLocalUtil;
import com.anyue.server.vo.UserVO;
import com.anyue.common.dto.Result;
import com.anyue.common.entity.User;
import com.anyue.common.utils.CommonUtil;
import com.anyue.common.utils.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result userRegister(UserVO userVO) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, userVO.getEmail()));
        if (user != null) {
            return Result.error("该邮箱已注册");
        }

        String userId = CommonUtil.generateRandomID(8);
        String token = JWTUtil.getToken(userId, userVO.getPassword());
        userMapper.insert(User.builder()
                .id(userId)
                .username(userVO.getUsername())
                .email(userVO.getEmail())
                .password(userVO.getPassword())
                .readingDuration(0)
                .followers(0)
                .likesCount(0)
                .token(token)
                .build());
        //存储用户ID到线程
        ThreadLocalUtil.setCurrentUser(userId);
        return Result.success().data(token);
    }

    @Override
    public Result userLogin(UserVO userVO) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, userVO.getEmail()));
        if (user == null) {
            return Result.error("该邮箱未注册");
        }
        if (!user.getPassword().equals(userVO.getPassword())) {
            return Result.error("密码错误");
        }

        String token = JWTUtil.getToken(user.getId(), userVO.getPassword());
        //存储用户ID到线程
        ThreadLocalUtil.setCurrentUser(user.getId());
        return Result.success().data(token);
    }

    @Override
    public Result getUserInfo() {
        User user = userMapper.selectById(ThreadLocalUtil.getCurrentUser());
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return Result.success().data(userDto);
    }
}
