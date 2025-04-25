package com.fullstack.demo.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Dependencies {

    @Column(name = "dependency_package")
    private String packageName;
    @Column(name = "dependency_version")
    private String version;

    public Dependencies() {
    }

    public Dependencies(String packageName, String version) {
        this.packageName = packageName;
        this.version = version;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
