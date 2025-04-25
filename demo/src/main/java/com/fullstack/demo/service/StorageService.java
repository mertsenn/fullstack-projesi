package com.fullstack.demo.service;

import java.io.InputStream;
import java.io.OutputStream;

public interface StorageService {
    void init() throws Exception;
    void store(String pkg, String version, String filename, InputStream data) throws Exception;
    void retrieve(String pkg, String version, String filename, OutputStream target) throws Exception;
}