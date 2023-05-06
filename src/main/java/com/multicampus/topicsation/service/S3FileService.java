package com.multicampus.topicsation.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.multicampus.topicsation.config.S3Configuration;

import java.io.IOException;

@Service
public class S3FileService implements IS3FileService {

    private final AmazonS3 s3Client;

    @Autowired
    public S3FileService(final AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Override
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

    @Override
    public void deleteFile(String bucketName, String objectKey) {
        s3Client.deleteObject(bucketName, objectKey);
    }

    @Override
    public boolean isFileExists(String bucketName, String folderName, String fileNameWithoutExtension) {
        String prefix = folderName + "/";
        ObjectListing objectListing = s3Client.listObjects(bucketName, prefix);

        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            String objectKey = objectSummary.getKey();

            String existingFileWithoutExtension;
            int prefixIndex = objectKey.indexOf(prefix);
            int dotIndex = objectKey.lastIndexOf(".");

            if (prefixIndex != -1 && dotIndex != -1 && dotIndex > prefixIndex) {
                existingFileWithoutExtension = objectKey.substring(prefixIndex + prefix.length(), dotIndex);
            } else {
                // 오류 처리 또는 기본값 설정
                existingFileWithoutExtension = "";
            }
            if (fileNameWithoutExtension.equalsIgnoreCase(existingFileWithoutExtension)) {
                // 기존 파일 삭제
                s3Client.deleteObject(bucketName, objectKey);
                return true;
            }
        }
        return false;
    }

    public String getImageUrl(String bucketName, String folderName, String fileNameWithoutExtension) {
        String prefix = folderName + "/";
        ObjectListing objectListing = s3Client.listObjects(bucketName, prefix);

        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            String objectKey = objectSummary.getKey();

            String existingFileWithoutExtension;
            int prefixIndex = objectKey.indexOf(prefix);
            int dotIndex = objectKey.lastIndexOf(".");

            if (prefixIndex != -1 && dotIndex != -1 && dotIndex > prefixIndex) {
                existingFileWithoutExtension = objectKey.substring(prefixIndex + prefix.length(), dotIndex);
            } else {
                // 오류 처리 또는 기본값 설정
                existingFileWithoutExtension = "";
            }

            if (fileNameWithoutExtension.equalsIgnoreCase(existingFileWithoutExtension)) {
                // 이미지 URL 생성
                String imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, s3Client.getRegion().toString(), objectKey);
                return imageUrl;
            }
        }

        // 파일이 존재하지 않으면 null 반환
        return null;
    }
}
