package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.utils.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
public class UploadController {

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    // --------------阿里云对象存储-----------------
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        log.info("上传文件名称: {}", file.getOriginalFilename());
        String url = aliyunOSSOperator.upload(file.getBytes(), Objects.requireNonNull(file.getOriginalFilename()));
        log.info("上传文件oss地址: {}", url);
        return Result.success(url);
    }
    // --------------本地磁盘存储-----------------
//    public Result upload(String name, Integer age, MultipartFile file) throws IOException {
//        log.info("上传文件: {}, 姓名: {}, 年龄: {}", file, name, age);
//
//        // 获取原始文件名
//        String originalFilename = file.getOriginalFilename();
//
//        assert originalFilename != null;  // assert 断言 确保不为空
//        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        // 文件名 UUID
//        String newFileName = UUID.randomUUID().toString() + extension;
//
//        // TODO: 处理上传的文件 保存
//        file.transferTo(new File("C:/Users/22390/Desktop/images/" + newFileName));
//        return Result.success();
//    }
}
