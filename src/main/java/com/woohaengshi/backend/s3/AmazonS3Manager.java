package com.woohaengshi.backend.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.woohaengshi.backend.config.S3Config;
import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AmazonS3Manager {
    private final AmazonS3 amazonS3;
    private final S3Config s3Config;

    public String uploadFile(String keyName, MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType("image/jpeg");
        try {
            amazonS3.putObject(
                    new PutObjectRequest(
                            s3Config.getBucket(), keyName, file.getInputStream(), metadata));
        } catch (IOException e) {
            throw new WoohaengshiException(ErrorCode.FAILED_SAVE_IMAGE);
        }

        return amazonS3.getUrl(s3Config.getBucket(), keyName).toString();
    }

    public String makeKeyName(Filepath filepath) {
        String uuid = UUID.randomUUID().toString();
        return filepath.toString() + '/' + uuid;
    }
}
