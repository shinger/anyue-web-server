package com.anread.user.controller;

import com.anread.common.dto.Result;
import com.anread.common.entity.UserAnnotation;
import com.anread.common.vo.UserAnnotationListVO;
import com.anread.user.service.UserAnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户标注接口
 */
@RestController
@RequestMapping("/user/annotation")
public class UserAnnotationController {

    @Autowired
    private UserAnnotationService userAnnotationService;

    /**
     * 上传用户标注
     * @param userAnnotation 用户标注
     * @return 响应
     */
    @PostMapping("/upload")
    public Result upload(@RequestBody UserAnnotation userAnnotation)  {
        userAnnotationService.upload(userAnnotation);
        return Result.success();
    }

    /**
     * 获取用户标注
     * @param userId 用户ID
     * @param bookId 书本ID
     * @return 获取用户标注
     */
    @GetMapping("/{bookId}")
    public Result<List<UserAnnotationListVO>> getUserAnnotations(@RequestHeader("X-User-ID") String userId, @PathVariable("bookId") String bookId)  {
        return userAnnotationService.getUserAnnotationsByBookId(userId, bookId);
    }
}
