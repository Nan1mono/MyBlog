package com.ljc.blog.controller;

import com.ljc.blog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {

//    @PostMapping
//    public Result upload(@RequestParam("image") MultipartFile file){        // spring中有专门接受文件的参数
//        // 获取原始文件名称
//        String originalFilename = file.getOriginalFilename();
//        // 为了保证文件的唯一性，需要用UUID重新设定文件名称
//        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, ".");
//    }
}
