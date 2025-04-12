package com.anyue.server.controller;

import com.anyue.common.enums.StateEnum;
import com.anyue.common.exception.BizException;
import com.anyue.server.service.UploadService;
import com.anyue.common.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "*")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/book")
    public Result uploadBook(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BizException(StateEnum.REQUEST_DATA_ERROR);
        }

        return uploadService.uploadBook(file);
    }

    @PostMapping("/image")
    public Result uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BizException(StateEnum.REQUEST_DATA_ERROR);
        }

        return uploadService.uploadImage(file);
    }


}
