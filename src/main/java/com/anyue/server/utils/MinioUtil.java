package com.anyue.server.utils;

import com.anyue.common.exception.BizException;
import com.anyue.common.entity.Book;
import com.anyue.common.enums.StateEnum;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.epublib.domain.MediaType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MinioUtil {

    private static volatile MinioClient minioClient = null;

    static  {
        try {
            minioClient = MinioClient.builder()
                    .endpoint("http://localhost:9000")
                    .credentials("admin", "password")
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("MinioClient创建失败", e);
        }
    }

    public static void uploadGeneralFile(String fileId, String filename, String contentType, byte[] fileBytes) {
        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileBytes);
        String suffix = filename.split("\\.")[1];
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("general") // 存储桶名称
                            .object(fileId + "." + suffix) // 文件名
                            .stream(fileInputStream, fileBytes.length, -1) // 字节数组输入流
                            .contentType(contentType) // MIME类型
                            .build()
            );
            log.info("==Minio上传【 General File 】内容：" + filename  + " 上传成功！==");
        }  catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }
    }

    public static String getEpubURL(Book book) {
        String presignedObjectUrl = null;
        try {
            presignedObjectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                    .bucket("books")
                    .object(book.getPath())
                    .expiry(1, TimeUnit.HOURS) // 3600s - 1h
                    .build());
        } catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }

        if (presignedObjectUrl == null) {
            log.error("Minio Epub文件URL生成失败：" + book.getTitle());
            throw new BizException(StateEnum.BUSSINESS_ERROR);
        }

        return presignedObjectUrl;
    }

    public static byte[] getEpub(Book book) {
        byte[] bytes = null;
        try {
            GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket("books")
                            .object(book.getPath())
                            .build()
            );
            bytes = response.readAllBytes();
        } catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    /**
     * 根据书名获取封面
     * @param bookName
     * @return
     */
    public static byte[] getCover(String bookName) {
        byte[] bytes = null;
        try {
            GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket("books")
                            .object(bookName + "/cover.jpeg")
                            .build()
            );
            bytes = response.readAllBytes();
        } catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    public static void uploadEpub(String fileId, String fileName, String contentType, byte[] coverBytes, byte[] fileBytes) {
        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileBytes);
        ByteArrayInputStream coverInputStream = new ByteArrayInputStream(coverBytes);
        String bookName = fileName.split("\\.")[0];
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("books") // 存储桶名称
                            .object(fileId + "/" + fileName) // 文件名
                            .stream(fileInputStream, fileBytes.length, -1) // 字节数组输入流
                            .contentType(contentType) // MIME类型
                            .build()
            );
            log.info("==Minio上传【 epub 】内容：" + fileName  + " 上传成功！==");
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("books") // 存储桶名称
                            .object(fileId + "/cover.jpeg") // 文件名
                            .stream(coverInputStream, coverBytes.length, -1) // 字节数组输入流
                            .contentType("image/jpeg") // MIME类型
                            .build()
            );
            log.info("==Minio上传【 封面 】内容：" + fileName + " 上传成功！==");
        }  catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将 epub 内容资源上传到 minio
     * @param index 资源序号
     * @param bookName 书名
     * @param mediaType 媒体类型
     * @param fileBytes 文件字节数据
     */
    public static void uploadBookContent(String bookName, int index, MediaType mediaType, byte[] fileBytes) {
        // 上传字节数组到MinIO
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("books") // 存储桶名称
                            .object(bookName + "/" + index + mediaType.getDefaultExtension()) // 文件名
                            .stream(byteArrayInputStream, fileBytes.length, -1) // 字节数组输入流
                            .contentType(mediaType.getName()) // MIME类型
                            .build()
            );
            log.info("==Minio上传【" + bookName + "】内容：" + index + mediaType.getDefaultExtension() + " " + bookName + " 上传成功！==");
        }  catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将 epub 资源上传到 minio
     * @param fileName 文件名
     * @param bookName 书名
     * @param mediaType 媒体类型
     * @param fileBytes 文件字节数组
     */
    public static void uploadBookResources(String bookName, String fileName,  MediaType mediaType, byte[] fileBytes) {
        // 上传字节数组到MinIO
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("books") // 存储桶名称
                            .object(bookName + "/" + fileName) // 文件名
                            .stream(byteArrayInputStream, fileBytes.length, -1) // 字节数组输入流
                            .contentType(mediaType.getName()) // MIME类型
                            .build()
            );
            log.info("==Minio上传【" + bookName + "】资源：" + fileName + " " + bookName + " 上传成功！==");
        }  catch (MinioException | NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }
    }
}
