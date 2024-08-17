package com.woohaengshi.backend.service.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    public String getUploadPreSignedUrl(String path) {
        GeneratePresignedUrlRequest uploadRequest = generatePresignedUrlRequest(bucket, path, HttpMethod.PUT);
        URL url = amazonS3.generatePresignedUrl(uploadRequest);
        return url.toString();
    }

    public String getDownloadPreSignedUrl(String path) {
        GeneratePresignedUrlRequest downloadRequest = generatePresignedUrlRequest(bucket, path, HttpMethod.GET);
        URL url = amazonS3.generatePresignedUrl(downloadRequest);
        return url.toString();
    }

    public static String createAndGetPath(String prefix, String filename) {
        String fileId = UUID.randomUUID().toString();
        return String.format("%s/%s", prefix, fileId + filename);
    }

    private GeneratePresignedUrlRequest generatePresignedUrlRequest(String bucket, String filename, HttpMethod method) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, filename)
                .withMethod(method)
                .withExpiration(getPreSignedUrlExpiration());
        if (method == HttpMethod.PUT) {
            request.addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());
        }
        return request;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);
        return expiration;
    }
}
