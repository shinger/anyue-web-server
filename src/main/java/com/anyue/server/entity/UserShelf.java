package com.anyue.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@TableName("user_shelf")
public class UserShelf {
    private String userId;
    private String bookId;
}
