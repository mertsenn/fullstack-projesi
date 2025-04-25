package com.fullstack.demo.controller;

import java.io.ByteArrayOutputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fullstack.demo.data.PackageEntity;
import com.fullstack.demo.repository.PackageRepository;
import com.fullstack.demo.service.StorageService;

@RestController
@RequestMapping("/")
public class PackageController {

    private final StorageService storage;
    private final PackageRepository repo;

    public PackageController(StorageService storage, PackageRepository repo) {
        this.storage = storage;
        this.repo = repo;
    }

    @PostMapping("/{name}/{version}")
    public ResponseEntity<?> deploy(
            @PathVariable("name") String name,
            @PathVariable("version") String version,
            @RequestPart("package") MultipartFile pkg,
            @RequestPart("meta") MultipartFile meta) throws Exception {

        if (!meta.getOriginalFilename().endsWith(".json") ||
            !pkg.getOriginalFilename().endsWith(".rep")) {
            return ResponseEntity.badRequest().body("Invalid file types.");
        }

        repo.findByNameAndVersion(name, version)
            .ifPresentOrElse(
                p -> {
                    throw new IllegalStateException("Package already exists.");
                },
                () -> {
                    PackageEntity entity = new PackageEntity();
                    entity.setName(name);
                    entity.setVersion(version);
                    repo.save(entity);
                }
            );
            

        storage.store(name, version, meta.getOriginalFilename(), meta.getInputStream());
        storage.store(name, version, pkg.getOriginalFilename(), pkg.getInputStream());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{name}/{version}/{file}")
public ResponseEntity<byte[]> download(
        @PathVariable("name") String name,
        @PathVariable("version") String version,
        @PathVariable("file") String file) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        storage.retrieve(name, version, file, out);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file + "\"")
                .body(out.toByteArray());
    }

}
