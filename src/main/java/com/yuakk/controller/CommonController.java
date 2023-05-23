package com.yuakk.controller;

import cn.hutool.core.lang.UUID;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author yuakk
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public String upload(MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() +suffix;
        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        file.transferTo(new File(basePath+fileName));
        return fileName;
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            FileInputStream fileInputStream = new FileInputStream(basePath + name);
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("/image/jpeg");

            int length;
            byte[] bytes = new byte[1024];
            while ((length=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,length);
                outputStream.flush();
            }
           outputStream.close();
           fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
