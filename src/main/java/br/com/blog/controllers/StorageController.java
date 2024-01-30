package br.com.blog.controllers;

import br.com.blog.service.StorageCloudinaryService;
import br.com.blog.service.impl.FileServiceIpml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/file")
public class StorageController {

    @Autowired
    private StorageCloudinaryService storageCloudinaryService;

    @Autowired
    private FileServiceIpml fileService;


    @PostMapping("/upload")
    public ResponseEntity<?> salvarImagemNaClaud(@RequestParam("files") List<MultipartFile> files) throws IOException {
        try {
            return ResponseEntity.ok().body(
                    fileService.salvarImagemNaClaud(files)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map> delete(@PathVariable("id") String id) throws IOException {
        try {
            return ResponseEntity.ok().body(storageCloudinaryService.delete(id));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}