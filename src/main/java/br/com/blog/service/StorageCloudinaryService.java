package br.com.blog.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface StorageCloudinaryService {

    Map upload(MultipartFile multipartFile) throws IOException;

    Map delete(String id) throws IOException;
}
