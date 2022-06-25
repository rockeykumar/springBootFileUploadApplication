package com.rajtech.springbootfileupload.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class AttachmentServiceImpl implements AttachmentService {


    @Value("${file.storage.location}")
    String fileStorageLocation;

    Path fileStoragePath;

    //    create directory
    @PostConstruct
    public void init() {
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException ioe) {
            throw new RuntimeException("Issue in creating in file directory");
        }
    }

    @Override
    public String saveAttachment(MultipartFile file) throws Exception {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path filePath = Paths.get(fileStoragePath + "/" +fileName);
        System.out.println("Path:" + filePath);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new RuntimeException("Issue in storing the file");
        }

        return fileName;
    }

    @Override
    public Resource downloadFile(String fileName) {

        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);
        System.out.println("Read Path: " + path);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue in reading the file");
        }

        if(resource.exists() && resource.isReadable())
            return resource;
        else
            throw new RuntimeException("File doesn't exist or not readable");

    }
}



