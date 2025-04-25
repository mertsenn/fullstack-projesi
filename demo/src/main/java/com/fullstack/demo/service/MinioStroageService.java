package com.fullstack.demo.service;

import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;

@Service
@ConditionalOnProperty(
  name = "storageStrategy",
  havingValue = "object-storage"
)
public class MinioStroageService implements StorageService{

    private final MinioClient client;
    @Value("${storage.minio.bucket}")
    private String bucket;

    public MinioStroageService(@Value("${storage.minio.url}") String url,
                               @Value("${storage.minio.access-key}") String accessKey,
                               @Value("${storage.minio.secret-key}") String secretKey) {
        this.client = MinioClient.builder()
                                 .endpoint(url)
                                 .credentials(accessKey, secretKey)
                                 .build();
    }

    @PostConstruct
    @Override
    public void init() throws Exception {
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    @Override
    public void store(String pkg, String version, String filename, InputStream data) throws Exception {
        String objectName = pkg + "/" + version + "/" + filename;
        client.putObject(
            PutObjectArgs.builder()
                         .bucket(bucket)
                         .object(objectName)
                         .stream(data, -1, 5 * 1024 * 1024)
                         .build()
        );
    }

    @Override
    public void retrieve(String pkg, String version, String filename, OutputStream target) throws Exception {
        String objectName = pkg + "/" + version + "/" + filename;
        try (InputStream in = client.getObject(
                 GetObjectArgs.builder().bucket(bucket).object(objectName).build())) {
            in.transferTo(target);
        }
    }
}
