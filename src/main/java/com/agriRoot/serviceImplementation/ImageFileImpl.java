package com.agriRoot.serviceImplementation;

import com.agriRoot.service.ImageFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class ImageFileImpl implements ImageFile {


    @Override
    public String uploadFile(MultipartFile multipartFile, String path) {
        String originalFilename = multipartFile.getOriginalFilename();
        log.info("Original fileName {}",originalFilename);

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        log.info("File extension {}",extension);

        String fileName = UUID.randomUUID().toString();
        String fileNameWithExtension = fileName + extension;
        String fullPathFileName = path + fileNameWithExtension;
        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {

            // Create the directory if it doesn't exist
            File file = new File(path);
            if (!file.exists()) {
               file.mkdirs();
            }

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, Paths.get(fullPathFileName), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file: " + e.getMessage());
            }

        } else {
            throw new RuntimeException("File with extension " + extension + " is not allowed");
        }

        return fileNameWithExtension; // Return the unique filename
    }

    @Override
    public InputStream getResource(String path, String name) {
        try {
            String fileInput = path + File.separator + name;
            InputStream inputStream = new FileInputStream(fileInput);
            return inputStream;
        }catch (Exception e){
            throw new RuntimeException("FIle is not found");
        }
    }

    @Override
    public void deleteImage(String path) {
        File file = new File(path);
        if (file.exists()) {
            boolean deleted = file.delete();  // Delete the file
            if (deleted) {
                log.info("File deleted successfully: {}", path);
            } else {
                log.warn("Failed to delete file: {}", path);
            }
        } else {
            log.warn("File does not exist: {}", path);
        }
    }
}
