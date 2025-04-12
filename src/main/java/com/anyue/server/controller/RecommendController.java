package com.anyue.server.controller;

import com.anyue.common.dto.Result;
import com.anyue.server.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    /**
     * 大家都在看推荐页列表
     * @return
     */
    @GetMapping("/common")
    private Result getCommonRecommend() {
        return recommendService.getCommonRecommend();
    }

}
