package com.anyue.server.controller;

import com.anyue.server.service.BookShelfService;
import com.anyue.common.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/bookshelf")
public class BookShelfController {

    @Autowired
    private BookShelfService bookShelfService;

    /**
     * 获取书架列表
     * @return
     */
    @GetMapping("/list")
    public Result getBookList() {
        return bookShelfService.getBookList();
    }

    /**
     * 添加到书架
     * @param bookId 书本ID
     * @return
     */
    @PostMapping("/join/{id}")
    public Result joinShelf(@PathVariable("id") String bookId) {
        return bookShelfService.join(bookId);
    }

    /**
     * 查找是否在书架内
     * @param bookId
     * @return
     */
    @GetMapping("/inshelf/{id}")
    public Result inShelf(@PathVariable("id") String bookId) {
        return bookShelfService.inShelf(bookId);
    }

    /**
     * 获取Epub内容
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getBookEpub(@PathVariable("id") String id) {
        return bookShelfService.getBookEpub(id);
    }

    /**
     * 移出书架
     * @param bookId
     * @return
     */
    @DeleteMapping("/remove/{id}")
    public Result removeShelf(@PathVariable("id") String bookId) {
        return bookShelfService.removeShelf(bookId);
    }

}
