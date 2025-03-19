/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.upload;

import com.hongyou.baron.ProjectProperties;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.web.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件
 *
 * @author Hong Bo Lin
 */
@RestController
@RequestMapping("/doc")
public class Document {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(Document.class);

    /**
     * 项目配置参数
     */
    private final ProjectProperties properties;

    /**
     * @param properties 项目配置参数
     */
    public Document(final ProjectProperties properties) {
        this.properties = properties;
    }

    /**
     * 文件上传
     *
     * @param multipart 文件信息
     * @param group 图片存储分组路径
     */
    @PostMapping("/upload")
    public ResponseEntity fileUpload(
        @RequestParam("file") MultipartFile multipart, @RequestParam("group") final String group
    ) {
        try {
            String fileName = UUID.randomUUID() + "_" + multipart.getOriginalFilename();
            fileName = group + "/" + fileName;

            // 检查文件夹是否存在
            File file = new File(this.properties.getUploadFilePath(), fileName);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    return ResponseEntity.builder().code(-1).message("文件创建失败").build();
                }
            }
            multipart.transferTo(file);
            return ResponseEntity.
                    builder().
                    code(ResponseEntity.SUCCESS_CODE).
                    message(fileName).
                    build();
        } catch (Exception e) {
            logger.error("文件上传失败", e);
            return ResponseEntity.builder().code(-1).message("文件上传失败").build();
        }
    }

    /**
     * 图片预览
     *
     * @param file 文件存储名
     */
    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public org.springframework.http.ResponseEntity<Resource> gallery(@RequestParam("file") final String file) {

        try {
            Path path = Paths.get(this.properties.getUploadFilePath()).resolve(file);
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
                return org.springframework.http.ResponseEntity.ok().body(resource);
            } else {
                throw new RestRuntimeException("图片加载失败");
            }
        } catch (Exception e) {
            logger.error("图片加载失败", e);
            return org.springframework.http.ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 文件下载
     *
     * @param file 文件存储名
     */
    @GetMapping(value = "/download", produces = MediaType.IMAGE_JPEG_VALUE)
    public org.springframework.http.ResponseEntity<Resource> download(@RequestParam("file") final String file) {

        try {
            Path path = Paths.get(this.properties.getUploadFilePath()).resolve(file);
            Resource resource = new UrlResource(path.toUri());

            // 文件下载头信息
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName().toString());
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            if (resource.exists() || resource.isReadable()) {
                return org.springframework.http.ResponseEntity.
                        ok().
                        headers(headers).
                        contentType(MediaType.APPLICATION_OCTET_STREAM).
                        body(resource);
            } else {
                throw new RestRuntimeException("文件加载失败");
            }
        } catch (Exception e) {
            logger.error("文件加载失败", e);
            return org.springframework.http.ResponseEntity.internalServerError().build();
        }
    }
}
