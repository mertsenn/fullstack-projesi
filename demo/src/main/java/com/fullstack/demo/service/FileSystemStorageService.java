package com.fullstack.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
@ConditionalOnProperty(
  name = "storageStrategy",
  havingValue = "file-system",
  matchIfMissing = true
)
public class FileSystemStorageService implements StorageService{

    @Value("${storage.fs.root-dir}")
    private String rootDir;

    @Override
    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(Path.of(rootDir));
    }

    @Override
    public void store(String pkg, String version, String filename, InputStream data) throws IOException {
        Path dir = Path.of(rootDir, pkg, version);
        Files.createDirectories(dir);
        Files.copy(data, dir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void retrieve(String pkg, String version, String filename, OutputStream target) throws IOException {
        Path file = Path.of(rootDir, pkg, version, filename);
        try (InputStream in = Files.newInputStream(file)) {
            in.transferTo(target);
        }
    }

}
