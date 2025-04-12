package com.anyue.server.controller;

import com.anyue.common.dto.Result;
import com.anyue.server.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/category/{categoryId}")
    public Result getBooks(@PathVariable("categoryId") String categoryId) {
        return bookService.getBooksByCategory(categoryId);
    }

    @GetMapping("/{bookId}")
    public Result getBook(@PathVariable("bookId") String bookId) {
        return bookService.getBookByBookId(bookId);
    }

}
