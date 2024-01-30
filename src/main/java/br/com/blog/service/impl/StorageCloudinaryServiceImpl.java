package br.com.blog.service.impl;

import br.com.blog.service.StorageCloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StorageCloudinaryServiceImpl implements StorageCloudinaryService {

    private final Cloudinary cloudinary;

    Map<String,String> valueMap = new HashMap<>();


    public StorageCloudinaryServiceImpl() {
        valueMap.put("cloud_name", "dljrcbp8l");
        valueMap.put("api_key", "137594888523843");
        valueMap.put("api_secret", "ne3rJmHC6o9yTQkAO5nGqPJFZDk");
        cloudinary = new Cloudinary(valueMap);
    }


    @Override
    public Map upload(MultipartFile multipartFile) throws IOException {
        File file = convert(multipartFile);
        Map upload = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        file.delete();
        return upload;
    }


    @Override
    public Map delete(String id) throws IOException {
        Map result = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
        return result;

    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fileOutputStream =new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();
        return file;
    }
}