package com.ruoyi.bussiness.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadFileOSS(MultipartFile file, String name) throws IOException;
}
