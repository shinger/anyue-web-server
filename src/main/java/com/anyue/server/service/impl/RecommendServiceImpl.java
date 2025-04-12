package com.anyue.server.service.impl;

import com.anyue.common.dto.Result;
import com.anyue.common.entity.Book;
import com.anyue.server.mapper.BookMapper;
import com.anyue.server.service.RecommendService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public Result getCommonRecommend() {
        long count = bookMapper.selectCount(null);
        int randomCount = (int) (Math.random()*count);
        if (randomCount > count - 4) {
            randomCount = (int) (count - 4);
        }
        // 随机获取四本书推荐
        List<Book> books = bookMapper.selectList(new QueryWrapper<Book>()
                .orderByAsc("id")
                .last("limit "+String.valueOf(randomCount)+", 4"));
        return Result.success().data(books);
    }
}
