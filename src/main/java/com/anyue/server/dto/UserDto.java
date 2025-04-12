package com.anyue.server.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserDto {
    private String email;
    private String username;
    private String avatar;
    private Integer readingDuration;
    private Integer followers;
    private Integer likesCount;
    public UserDto() {}
}
