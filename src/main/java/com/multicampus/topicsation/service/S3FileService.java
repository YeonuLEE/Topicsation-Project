package com.multicampus.topicsation.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.multicampus.topicsation.config.S3Configuration;

import java.io.IOException;

@Service
public class S3FileService {

    private final AmazonS3 s3Client;

    @Autowired
    public S3FileService(final AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadFile(String bucketName, String objectKey, MultipartFile file) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, file.getInputStream(), objectMetadata);

            s3Client.putObject(putObjectRequest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("File upload failed", e);
        }
    }
}
