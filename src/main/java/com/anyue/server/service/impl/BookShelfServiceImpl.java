package com.anyue.server.service.impl;

import com.anyue.common.entity.Book;
import com.anyue.server.entity.UserShelf;
import com.anyue.server.mapper.BookMapper;
import com.anyue.server.mapper.UserShelfMapper;
import com.anyue.server.service.BookShelfService;
import com.anyue.server.utils.MinioUtil;
import com.anyue.common.dto.Result;
import com.anyue.server.utils.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class BookShelfServiceImpl implements BookShelfService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private UserShelfMapper userShelfMapper;

    @Override
    public Result getBookList() {
        String userId = ThreadLocalUtil.getCurrentUser();
        LambdaQueryWrapper<UserShelf> queryWrapper = new LambdaQueryWrapper<UserShelf>()
                .eq(UserShelf::getUserId, userId)
                .select(UserShelf::getBookId);

        List<String> bookIds = userShelfMapper.selectObjs(queryWrapper);
        List<Book> books = bookMapper.selectList(new LambdaQueryWrapper<Book>().in(Book::getId, bookIds));
        return Result.success().data(books);
    }

    @Override
    public Result getBookEpub(String id) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            return null;
        }

        String epubURL = MinioUtil.getEpubURL(book);
        return Result.success().data(epubURL);
    }

    @Override
    public Result join(String bookId) {
        String userId = ThreadLocalUtil.getCurrentUser();
        // 查找书架是否存在
        UserShelf userShelf = userShelfMapper.selectOne(new LambdaQueryWrapper<UserShelf>()
                .eq(UserShelf::getUserId, userId)
                .eq(UserShelf::getBookId, bookId));
        if (userShelf != null) {
            return Result.error("已在书架内");
        }
        // 插入用户-书架表
        userShelfMapper.insert(UserShelf.builder()
                .bookId(bookId)
                .userId(userId)
                .build());
        // 更新阅读人数+1
        bookMapper.incrementReadership(bookId);
        return Result.success().message("已加入书架");
    }

    @Override
    public Result inShelf(String bookId) {
        String userId = ThreadLocalUtil.getCurrentUser();
        // 查找书架是否存在
        UserShelf userShelf = userShelfMapper.selectOne(new LambdaQueryWrapper<UserShelf>()
                .eq(UserShelf::getUserId, userId)
                .eq(UserShelf::getBookId, bookId));

        return Result.success().data(userShelf != null);
    }

    @Override
    public Result removeShelf(String bookId) {
        String userId = ThreadLocalUtil.getCurrentUser();

        int delete = userShelfMapper.delete(new LambdaQueryWrapper<UserShelf>()
                .eq(UserShelf::getBookId, bookId)
                .eq(UserShelf::getUserId, userId));

        if (delete == 0) {
            return Result.error("移出书架失败");
        }
        return Result.success().data("已移出书架");
    }
}
