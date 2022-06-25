package com.rajtech.springbootfileupload.controller;


import com.rajtech.springbootfileupload.model.ResponseData;
import com.rajtech.springbootfileupload.service.AttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
public class AttachmentController {

    private AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }


    @PostMapping(value = "/upload", produces = "application/json")
    public ResponseData uploadFile(@RequestParam("file")MultipartFile file) throws Exception {

        String downloadURI = "";
        String fileAName = attachmentService.saveAttachment(file);

        downloadURI = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileAName)
                .toUriString();

        return new ResponseData(fileAName, downloadURI, file.getContentType(), file.getSize());

    }

    @GetMapping("/download/{fileName}")
    ResponseEntity<Resource> downloadSingleFile(@PathVariable String fileName) {

        Resource resource = attachmentService.downloadFile(fileName);

        MediaType contentType = MediaType.IMAGE_JPEG;

        return ResponseEntity.ok()
                .contentType(contentType)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName="+resource.getFilename())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName="+resource.getFilename())
                .body(resource);
    }


}
