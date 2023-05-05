package com.multicampus.topicsation.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

@Configuration
public class S3Configuration {
    @Value("${ncp.objectstorage.endpoint}")
    private String endPoint;

    @Value("${ncp.objectstorage.region}")
    private String regionName;

    @Value("${ncp.objectstorage.access-key}")
    private String accessKey;

    @Value("${ncp.objectstorage.secret-key}")
    private String secretKey;

    @Bean
    public AmazonS3 s3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

        return s3Client;
    }

}
