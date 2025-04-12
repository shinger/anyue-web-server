package com.anyue.server.service;


import com.anyue.common.dto.Result;

public interface CategoryService {
    Result getCategories(Integer parentId);
}
