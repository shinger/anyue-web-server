package com.anread.user.service;

import com.anread.common.dto.Result;
import com.anread.common.entity.UserAnnotation;
import com.anread.common.vo.UserAnnotationListVO;

import java.util.List;

public interface UserAnnotationService {
    /**
     * 上传用户标注
     * @param userAnnotation 用户标注
     */
    Result upload(UserAnnotation userAnnotation);
    /**
     * 获取用户标注
     * @param userId 用户ID
     * @param bookId 书本ID
     * @return 用户标注
     */
    Result<List<UserAnnotationListVO>> getUserAnnotationsByBookId(String userId, String bookId);
}
