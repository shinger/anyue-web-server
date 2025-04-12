package com.anyue.server.service;

import com.anyue.common.dto.Result;

public interface BookService {

    Result getBook(String bookId);

    Result getBooksByCategory(String categoryId);

    Result getBookByBookId(String bookId);
}
