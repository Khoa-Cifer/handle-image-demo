package com.imagetesting.handleimagedemo.service;

import com.imagetesting.handleimagedemo.entity.ImageData;
import com.imagetesting.handleimagedemo.repository.ImageDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {
    @Autowired
    private ImageDataRepository repository;
    private final String FOLDER_PATH = "I:/JavaProject/SpringBoot/handle-image-demo/src/main/java/com/imagetesting/handleimagedemo/data/";

    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String filePath = FOLDER_PATH + file.getOriginalFilename();
        Date currentDate = new Date();
        ImageData fileData = new ImageData();
        int versionCopy = 0;
        for (int i = 0; i < getTotalImageInFileSystem(); i++) {
            if (file.getOriginalFilename().equalsIgnoreCase(repository.getDuplicateImageName().get(i))) {
                versionCopy++;
            }
        }

        fileData.setName(file.getOriginalFilename());
        fileData.setCreatedTime(currentDate);
        fileData.setType(file.getContentType());
        fileData.setFilePath(filePath);
        fileData.setVersionCopy(versionCopy);

        repository.save(fileData);

        if (fileData.getVersionCopy() == 0) {
            file.transferTo(new File(FOLDER_PATH + file.getOriginalFilename()));
        } else {
            file.transferTo(new File(FOLDER_PATH + file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf('.'))
                    + " " + "(" + fileData.getVersionCopy() + ")" + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'))));
        }

        if (fileData != null) {
            return "file uploaded successfully : " + filePath;
        }
        return null;
    }

    public byte[] getImageFromFileSystem(Long id) throws IOException {
        Optional<ImageData> fileData = repository.findById(id);
        String filePath = fileData.get().getFilePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }

    public String updateImageInFileSystem(String fileName, MultipartFile file) throws IOException {
        Optional<ImageData> fileData = repository.findByName(fileName);
        String filePath = FOLDER_PATH + file.getOriginalFilename();

        ImageData imageData = fileData.get();
        imageData.setName(file.getOriginalFilename());
        imageData.setType(file.getContentType());
        imageData.setFilePath(filePath);

        file.transferTo(new File(FOLDER_PATH + imageData.getId() + "_" + file.getOriginalFilename()));
        repository.save(imageData);

        if (imageData != null) {
            return "file modified successfully : " + filePath;
        }
        return null;
    }

    public int getTotalImageInFileSystem() {
        return repository.getTotalImages();
    }

    public List<String> getDuplicatedImageInFileSystem() {
        return repository.getDuplicateImageName();
    }
}
