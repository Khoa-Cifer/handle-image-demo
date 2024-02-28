package com.imagetesting.handleimagedemo.controller;

import com.imagetesting.handleimagedemo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageService service;

    @PostMapping("/fileSystem")
    public ResponseEntity<String> uploadImageToFIleSystem(@RequestParam("image")MultipartFile file) throws IOException {
        String uploadImage = service.uploadImageToFileSystem(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/fileSystem/{id}")
    public ResponseEntity<byte[]> downloadImageFromFileSystem(@PathVariable Long id) throws IOException {
        byte[] imageData=service.getImageFromFileSystem(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);

    }

    @PutMapping("/fileSystem/update/{fileName}")
    public ResponseEntity<String> deleteImageInFileSystem(@PathVariable String fileName, @RequestParam("image")MultipartFile file) throws IOException {
    String imagePath = service.updateImageInFileSystem(fileName, file);
    return ResponseEntity.status(HttpStatus.OK)
                .body(imagePath);
    }
}
