package com.anyue.server.service.impl;

import com.anyue.common.entity.GeneralFile;
import com.anyue.common.entity.User;
import com.anyue.common.utils.MD5Uitl;
import com.anyue.server.dto.FileDto;
import com.anyue.common.enums.StateEnum;
import com.anyue.common.entity.BookFile;
import com.anyue.server.mapper.FileMapper;
import com.anyue.server.mapper.GeneralFileMapper;
import com.anyue.server.mapper.UserMapper;
import com.anyue.server.service.UploadService;
import com.anyue.server.utils.EpublibUtil;
import com.anyue.server.utils.MinioUtil;
import com.anyue.common.dto.Result;
import com.anyue.server.utils.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private GeneralFileMapper generalFileMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result uploadBook(MultipartFile file) {
        // 读取文件字节数组
        byte[] fileBytes = null;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 获取文件MD5
        String md5 = MD5Uitl.generateMD5(fileBytes);
        // 判断文件是否存在
        boolean exists = fileMapper.exists(new LambdaQueryWrapper<BookFile>()
                .eq(BookFile::getId, md5));
        if (exists) {
            FileDto fileDto = FileDto.builder()
                    .id(md5)
                    .coverImg("http://127.0.0.1:9000/books/" + md5 + "/cover.jpeg")
                    .build();
            // 返回
            return Result.success().data(fileDto).state(StateEnum.UPLOAD_SUCCESS);
        }

        // 文件名
        String filename = file.getOriginalFilename();
        // 书名
        String bookName = filename.split("\\.")[0];
        // 文件类型
        String contentType = file.getContentType();
        // 提取封面字节
        byte[] coverBytes = EpublibUtil.extractCover(file);
        // 存入Minio
        MinioUtil.uploadEpub(md5, filename, contentType, coverBytes, fileBytes);

        fileMapper.insert(BookFile.builder()
                .id(md5)
                .filename(filename)
                .bucket("books")
                .path(md5 + "/" + filename)
                .build());

        log.info("==Mysql Insert File " + md5 + "成功！==");

        FileDto fileDto = FileDto.builder()
                .id(md5)
                .coverImg("http://127.0.0.1:9000/books/" + md5 + "/cover.jpeg")
                .build();
        // 返回
        return Result.success().data(fileDto).state(StateEnum.UPLOAD_SUCCESS);
    }

    @Override
    public Result uploadImage(MultipartFile file) {
        // 读取文件字节数组
        byte[] fileBytes = null;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 获取文件MD5
        String md5 = MD5Uitl.generateMD5(fileBytes);
        // 判断文件是否存在
        GeneralFile generalFile = generalFileMapper.selectById(md5);
        if (generalFile != null) {
            return Result.success().data(generalFile.getImageUrl());
        }

        // 文件名
        String filename = file.getOriginalFilename();
        // 文件类型
        String contentType = file.getContentType();
        // 存入Minio
        MinioUtil.uploadGeneralFile(md5, filename, contentType, fileBytes);

        String imgUrl = "http://127.0.0.1:9000/general/" + md5 + "." + filename.split("\\.")[1];

        generalFileMapper.insert(GeneralFile.builder()
                .id(md5)
                .filename(filename)
                .path(md5 + "/" + filename)
                .referenceCount(0)
                .imageUrl(imgUrl)
                .build());

        log.info("==Mysql Insert File " + filename + "成功！==");

        userMapper.update(new LambdaUpdateWrapper<User>()
                .eq(User::getId, ThreadLocalUtil.getCurrentUser())
                .set(User::getAvatar, imgUrl));

        // 返回
        return Result.success().data(imgUrl).state(StateEnum.UPLOAD_SUCCESS);
    }
}
