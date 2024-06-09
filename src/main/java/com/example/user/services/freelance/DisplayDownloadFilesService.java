package com.example.user.services.freelance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class DisplayDownloadFilesService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Resource downloadFile( String fileName) {
        Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
        Resource resource = new FileSystemResource(filePath.toFile());

        if (!resource.exists()) {
            return null;
        }
        return resource;
    }

    public byte[] displayImage( String fileName) throws IOException {
        Path imagePath = Paths.get(uploadDir).resolve(fileName).normalize();
        byte[] imageBytes;
        return  Files.readAllBytes(imagePath);
    }




}
