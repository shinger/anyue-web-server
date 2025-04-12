package com.anyue.server.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 文件上传返回前端
 */
@Data
@Builder
public class FileDto {
    private String id;
    private String coverImg;
}
