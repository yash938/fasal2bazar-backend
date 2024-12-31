package com.agriRoot.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface  ImageFile {

    String uploadFile(MultipartFile multipartFile,String path);
    InputStream getResource(String path,String name);
    void deleteImage(String path);
}
