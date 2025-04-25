package com.fullstack.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fullstack.demo.data.PackageEntity;

public interface PackageRepository extends JpaRepository<PackageEntity, Long>{
    Optional<PackageEntity> findByNameAndVersion(String name, String version);

}
