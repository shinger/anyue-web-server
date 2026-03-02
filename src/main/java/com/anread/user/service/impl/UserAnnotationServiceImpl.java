package com.anread.user.service.impl;

import com.anread.common.dto.Result;
import com.anread.common.entity.UserAnnotation;
import com.anread.common.enums.StateEnum;
import com.anread.common.utils.CommonUtil;
import com.anread.common.vo.UserAnnotationListVO;
import com.anread.common.vo.UserAnnotationVO;
import com.anread.user.mapper.UserAnnotationMapper;
import com.anread.user.service.UserAnnotationService;
import com.anread.user.utils.CfiComparators;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户标注服务实现类
 */
@Slf4j
@Service
public class UserAnnotationServiceImpl implements UserAnnotationService {

    @Autowired
    private UserAnnotationMapper userAnnotationMapper;

    @Override
    public Result upload(UserAnnotation userAnnotation) {
        if (userAnnotation == null ||
                userAnnotation.getUserId() == null ||
                userAnnotation.getBookId() == null ||
                userAnnotation.getEpubCfiRange() == null ||
                userAnnotation.getType() == null) {
            log.error("【上传标注】参数为空");
            return Result.error(StateEnum.PARAMS_ERROR);
        }
        UserAnnotation existAnnotation = userAnnotationMapper.selectOne(new LambdaQueryWrapper<UserAnnotation>()
                .eq(UserAnnotation::getEpubCfiRange, userAnnotation.getEpubCfiRange()));
        if (existAnnotation != null) {
            // 更新已存在的标注
            existAnnotation.setType(userAnnotation.getType());
            existAnnotation.setLineColor(userAnnotation.getLineColor());
            existAnnotation.setIdeaContent(userAnnotation.getIdeaContent());
            userAnnotationMapper.updateById(existAnnotation);
        } else {
            // 插入新标注
            userAnnotation.setId(CommonUtil.generateRandomID(32));
            userAnnotationMapper.insert(userAnnotation);
        }
        log.info("【上传标注】成功，数据：{}", userAnnotation);
        return Result.success();
    }

    @Override
    public Result<List<UserAnnotationListVO>> getUserAnnotationsByBookId(String userId, String bookId) {
        List<UserAnnotationListVO> userAnnotationListVOS = userAnnotationMapper.getUserAnnotationsInToc(userId, bookId);
        if (userAnnotationListVOS != null && userAnnotationListVOS.size() > 0) {
            for (UserAnnotationListVO annotationVO : userAnnotationListVOS) {
                if (annotationVO.getUserAnnotationList() != null && annotationVO.getUserAnnotationList().size() > 0) {
                    annotationVO.getUserAnnotationList().sort(CfiComparators.byCfi(UserAnnotationVO::getEpubCfiRange));
                }
            }
        }
        return Result.<List<UserAnnotationListVO>>success().data(userAnnotationListVOS);
    }
}
