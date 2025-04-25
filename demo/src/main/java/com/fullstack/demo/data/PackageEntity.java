package com.fullstack.demo.data;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "packages")
public class PackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String version;
    private String author;
    
    @ElementCollection
    @CollectionTable(
      name = "package_dependencies",
      joinColumns = @JoinColumn(name = "package_id")
    )
    private List<Dependencies> dependencies;

    public PackageEntity() {
    }
    public PackageEntity(Long id, String name, String version, String author, List<Dependencies> dependencies) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.author = author;
        this.dependencies = dependencies;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public List<Dependencies> getDependencies() {
        return dependencies;
    }
    public void setDependencies(List<Dependencies> dependencies) {
        this.dependencies = dependencies;
    }
    


}
