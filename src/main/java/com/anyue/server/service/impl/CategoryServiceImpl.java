package com.anyue.server.service.impl;

import com.anyue.common.dto.Result;
import com.anyue.common.entity.Category;
import com.anyue.server.mapper.CategoryMapper;
import com.anyue.server.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result getCategories(Integer parentId) {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<Category>().eq(Category::getParentId, parentId));
        return Result.success().data(categories);
    }
}
