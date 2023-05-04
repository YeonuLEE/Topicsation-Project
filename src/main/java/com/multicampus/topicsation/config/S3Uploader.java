package com.multicampus.topicsation.config;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URI;
import java.util.List;


public class S3Uploader {
    public static void main(String[] args) {
        final String endPoint = "https://kr.object.ncloudstorage.com";
        final String regionName = "kr-standard";
        final String accessKey = "iN53p6VCjKdwUMfOrCwG";
        final String secretKey = "xei6FkB8VoyYM0zg9UqV05RDLyCTFZg8kg4cIo3G";

        // S3 client
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();

//        try {
//            List<Bucket> buckets = s3.listBuckets();
//            System.out.println("Bucket List: ");
//            for (Bucket bucket : buckets) {
//                System.out.println("    name=" + bucket.getName() + ", creation_date=" + bucket.getCreationDate() + ", owner=" + bucket.getOwner().getId());
//            }
//        } catch (AmazonS3Exception e) {
//            e.printStackTrace();
//        } catch(SdkClientException e) {
//            e.printStackTrace();
//        }


        //파일 업로드
        String bucketName = "asset";
        String folderName = "profile/";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0L);
        objectMetadata.setContentType("application/x-directory");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, new ByteArrayInputStream(new byte[0]), objectMetadata);

        try {
            s3.putObject(putObjectRequest);
            System.out.format("Folder %s has been created.\n", folderName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }

        // upload local file
        String objectName = "sample-object2";
        String filePath = "/Users/yeonu/Y_DEV/profile/2.jpg";

        try {
            s3.putObject(bucketName, objectName, new File(filePath));
            System.out.format("Object %s has been created.\n", objectName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }
}
