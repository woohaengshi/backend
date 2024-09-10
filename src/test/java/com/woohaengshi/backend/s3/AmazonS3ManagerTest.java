package com.woohaengshi.backend.s3;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.woohaengshi.backend.config.S3Config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

class AmazonS3ManagerTest {
    @Mock private AmazonS3 amazonS3;

    @Mock private S3Config s3Config;

    @InjectMocks private AmazonS3Manager amazonS3Manager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 파일_업로드_성공() throws Exception {
        MultipartFile mockFile =
                new MockMultipartFile(
                        "file", "test-image.jpg", "image/jpeg", "test-image-content".getBytes());

        String bucketName = "test-bucket";
        String keyName = "test-key";

        when(s3Config.getBucket()).thenReturn(bucketName);
        when(amazonS3.getUrl(bucketName, keyName))
                .thenReturn(new URL("https://s3.amazonaws.com/test-bucket/test-key"));

        String resultUrl = amazonS3Manager.uploadFile(keyName, mockFile);

        verify(amazonS3, times(1)).putObject(any(PutObjectRequest.class));
        verify(amazonS3, times(1)).getUrl(bucketName, keyName);

        assertEquals("https://s3.amazonaws.com/test-bucket/test-key", resultUrl);
    }
}
