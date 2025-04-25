package com.fullstack.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fullstack.demo.service.FileSystemStorageService;
import com.fullstack.demo.service.MinioStroageService;
import com.fullstack.demo.service.StorageService;


@Configuration
public class StorageConfig {

    @Value("${storageStrategy:file-system}")
    private String strategy;

    @Bean
    @Primary
    public StorageService storageService(
            FileSystemStorageService fs,
            @Autowired(required = false) MinioStroageService minio) {

                if ("object-storage".equals(strategy)) {
                    if (minio == null) {
                        throw new IllegalStateException("Object-storage seçildi ama MinioService bulunamadı");
                    }
                    return minio;
                } else {
                    return fs;
                }
    }
}


