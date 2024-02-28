package com.imagetesting.handleimagedemo.repository;

import com.imagetesting.handleimagedemo.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageDataRepository extends JpaRepository<ImageData, Long> {
    Optional<ImageData> findByName(String fileName);
    @Query("SELECT i.name FROM ImageData i")
    List<String> getDuplicateImageName();

    @Query("SELECT COUNT(*) FROM ImageData i")
    int getTotalImages();
}
