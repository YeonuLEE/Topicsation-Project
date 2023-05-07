package com.multicampus.topicsation.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.multicampus.topicsation.config.S3Configuration;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

@Service
public class S3FileService implements IS3FileService {

    private final AmazonS3 s3Client;
    private final S3Configuration s3Configuration;

    @Autowired
    public S3FileService(final AmazonS3 s3Client, final S3Configuration s3Configuration) {
        this.s3Client = s3Client;
        this.s3Configuration = s3Configuration;
    }

    @Override
    public void uploadFile(String bucketName, String objectKey, MultipartFile file) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, file.getInputStream(), objectMetadata);

            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead); //공개권한 설정

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

    @Override
    public String getImageUrl(String bucketName, String folderName, String imgId) {
        String prefix = folderName + "/";
        ObjectListing objectListing = s3Client.listObjects(bucketName, prefix);
        System.out.println(imgId);
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            String objectKey = objectSummary.getKey();
            int prefixIndex = objectKey.indexOf(prefix);
            String fileName = objectKey.substring(prefixIndex + prefix.length());

            if (fileName.equals(imgId)) {
                String endPoint = s3Configuration.getEndPoint();
                if (!endPoint.startsWith("https://")) {
                    endPoint = "https://" + endPoint;
                }
                String imageUrl = String.format("%s/%s/%s", endPoint, bucketName, objectKey);
                System.out.println(imageUrl);
                return imageUrl;
            }
        }
        // 파일이 존재하지 않으면 null 반환
        return null;
    }
    @Override
    public URL generatePresignedUrl(String bucketName, String objectKey) {
        Date expiration = new Date();
        long msec = expiration.getTime();
        msec += 1000 * 60 * 60; // 1 hour.
        expiration.setTime(msec);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return url;
    }
}
