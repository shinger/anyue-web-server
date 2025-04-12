package com.anyue.server.utils;

import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

public class EpublibUtil {

    public static byte[] extractCover(MultipartFile file) {
        InputStream in = null;
        byte[] data = null;
        try {
            EpubReader reader = new EpubReader();
            in = file.getInputStream();
            Book book = reader.readEpub(in);
            if (book.getCoverImage() != null) {
                data = book.getCoverImage().getData();
            } else if (book.getResources().getById("cover.jpg") != null) {
                data =  book.getResources().getById("cover.jpg").getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static void parse2Minio(MultipartFile file) {
        boolean hasToc = false; // 是否存在目录
        InputStream in = null;
        try {
            EpubReader reader = new EpubReader();
            in = file.getInputStream();
            Book book = reader.readEpub(in);
            Resources resources = book.getResources(); // 全部资源
            // 书脊 - 阅读顺序 - 分章节存储
            List<SpineReference> spineReferences = book.getSpine().getSpineReferences();
            Resource tocResource = book.getSpine().getTocResource();
            // 存在目录资源toc.ncx
            if (tocResource != null) {
                hasToc = true;
            }
            for (int i = 0; i < spineReferences.size(); i++) {
                Resource resource = spineReferences.get(i).getResource();
                resources.remove(resource.getHref()); // 去除全部资源当中的内容部分
                // 将内容部分按序号放入minio当中
                MinioUtil.uploadBookContent(file.getName(), i, resource.getMediaType(), resource.getData());
            }
            // 把剩余的资源存储到minio当中
            Collection<String> allHrefs = resources.getAllHrefs();
            for (String href : allHrefs) {
                Resource resource = resources.getByHref(href);
                if (resource.getId().equals("nav")) {
                    hasToc = true;
                }
                MinioUtil.uploadBookResources(file.getName(), href, resource.getMediaType(), resource.getData());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
