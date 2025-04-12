package com.anyue.server.service;

import com.anyue.common.dto.Result;

import javax.servlet.http.HttpServletRequest;

public interface BookShelfService {

    /**
     * 获取书架列表
     * @return
     */
    public Result getBookList();

    /**
     * 获取书籍Epub文件
     * @return
     */
    public Result getBookEpub(String id);

    /**
     * 加入书架
     * @param bookId
     * @return
     */
    Result join(String bookId);

    /**
     * 查找是否在书架内
     * @param bookId
     * @return
     */
    Result inShelf(String bookId);

    /**
     * 移出书架
     * @param bookId
     * @return
     */
    Result removeShelf(String bookId);
}
