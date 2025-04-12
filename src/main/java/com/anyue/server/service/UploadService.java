package com.anyue.server.service;

import com.anyue.common.dto.Result;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    /**
     * 上传书本
     * @param file Epub格式文件
     */
    Result uploadBook(MultipartFile file);

    /**
     * 上传图片
     * @param file
     * @return
     */
    Result uploadImage(MultipartFile file);
}
