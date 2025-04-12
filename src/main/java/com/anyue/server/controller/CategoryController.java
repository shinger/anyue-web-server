package com.anyue.server.controller;

import com.anyue.common.dto.Result;
import com.anyue.server.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{parentId}")
    public Result getCategories(@PathVariable Integer parentId) {
        return categoryService.getCategories(parentId);
    }

}
