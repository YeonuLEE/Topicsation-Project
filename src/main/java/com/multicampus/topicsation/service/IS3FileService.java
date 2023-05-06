package com.multicampus.topicsation.service;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.web.multipart.MultipartFile;

public interface IS3FileService {
    void uploadFile(String bucketName, String objectKey, MultipartFile file);
    void deleteFile(String bucketName, String objectKey);
    boolean isFileExists(String bucketName, String folderName, String fileNameWithoutExtension);
    String getImageUrl(String bucketName, String folderName, String imgId);
}
