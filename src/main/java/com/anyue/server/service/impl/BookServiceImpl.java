package com.anyue.server.service.impl;

import com.anyue.common.dto.Result;
import com.anyue.common.entity.Book;
import com.anyue.common.entity.Category;
import com.anyue.common.enums.StateEnum;
import com.anyue.common.exception.BizException;
import com.anyue.server.mapper.BookMapper;
import com.anyue.server.mapper.CategoryMapper;
import com.anyue.server.service.BookService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result getBook(String bookId) {
        Book book = bookMapper.selectById(bookId);
        if (book == null) {
            throw new BizException("书本不存在");
        }
        return Result.success().data(book);
    }

    @Override
    public Result getBooksByCategory(String categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        List<Book> books = null;
        if (category.getParentId() == 0) { // 获取主分类的全部书本
            List<Category> subCategories = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                    .eq(Category::getParentId, categoryId));
            List<Integer> subCategoryIds = subCategories.stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
            books = bookMapper.selectList(new LambdaQueryWrapper<Book>().in(Book::getCategoryId, subCategoryIds));
        } else {
            books = bookMapper.selectList(new LambdaQueryWrapper<Book>()
                    .eq(Book::getCategoryId, categoryId));
        }

        return Result.success().data(books);
    }

    @Override
    public Result getBookByBookId(String bookId) {
        Book book = bookMapper.selectById(bookId);
        if (book == null) {
            return Result.error(StateEnum.BOOK_NOT_EXISTS);
        }
        return Result.success().data(book);
    }
}
