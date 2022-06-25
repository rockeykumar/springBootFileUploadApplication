package com.rajtech.springbootfileupload.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    String saveAttachment(MultipartFile file) throws Exception;

    Resource downloadFile(String fileName);
}
