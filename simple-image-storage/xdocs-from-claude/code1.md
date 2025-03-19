# Simple Image Storage System

A Spring Boot 3 application for storing, retrieving, and managing images.

## Project Structure

```
image-storage-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── imagestorage/
│   │   │               ├── ImageStorageApplication.java
│   │   │               ├── controller/
│   │   │               │   └── ImageController.java
│   │   │               ├── service/
│   │   │               │   ├── ImageService.java
│   │   │               │   └── ImageServiceImpl.java
│   │   │               ├── repository/
│   │   │               │   └── ImageRepository.java
│   │   │               ├── model/
│   │   │               │   └── Image.java
│   │   │               ├── exception/
│   │   │               │   ├── ImageNotFoundException.java
│   │   │               │   └── GlobalExceptionHandler.java
│   │   │               └── config/
│   │   │                   └── StorageConfig.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       └── templates/
│   │           ├── index.html
│   │           └── upload.html
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── imagestorage/
│                       ├── controller/
│                       │   └── ImageControllerTest.java
│                       └── service/
│                           └── ImageServiceTest.java
└── pom.xml
```

## Dependencies (pom.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.3</version>
        <relativePath/>
    </parent>
    
    <groupId>com.example</groupId>
    <artifactId>image-storage</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>image-storage</name>
    <description>Simple Image Storage System</description>
    
    <properties>
        <java.version>21</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
        <!-- H2 Database for development -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- For image metadata extraction -->
        <dependency>
            <groupId>com.drewnoakes</groupId>
            <artifactId>metadata-extractor</artifactId>
            <version>2.19.0</version>
        </dependency>
        
        <!-- Lombok for boilerplate reduction -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

## Application Entry Point

### ImageStorageApplication.java

```java
package com.example.imagestorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImageStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageStorageApplication.class, args);
    }
}
```

## Model

### Image.java

```java
package com.example.imagestorage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "images")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String originalFilename;
    private String contentType;
    private Long size;
    private String path;
    
    // Store metadata as JSON or specific fields
    private String dimensions;
    private String description;
    
    // Additional metadata
    private LocalDateTime uploadedAt;
    private LocalDateTime modifiedAt;
    
    // For small images, could store directly in DB
    // For production, better to use file storage and keep only metadata in DB
    @Lob
    private byte[] data;
}
```

## Repository

### ImageRepository.java

```java
package com.example.imagestorage.repository;

import com.example.imagestorage.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    
    List<Image> findByNameContainingIgnoreCase(String name);
    
    List<Image> findByContentType(String contentType);
    
    List<Image> findByOrderByUploadedAtDesc();
}
```

## Service

### ImageService.java

```java
package com.example.imagestorage.service;

import com.example.imagestorage.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    Image store(MultipartFile file, String description) throws IOException;
    
    Image getImage(Long id);
    
    List<Image> getAllImages();
    
    List<Image> searchImages(String keyword);
    
    List<Image> getImagesByType(String contentType);
    
    void deleteImage(Long id);
    
    Image updateImage(Long id, String description) throws IOException;
}
```

### ImageServiceImpl.java

```java
package com.example.imagestorage.service;

import com.example.imagestorage.exception.ImageNotFoundException;
import com.example.imagestorage.model.Image;
import com.example.imagestorage.repository.ImageRepository;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    
    @Value("${app.storage.location:uploads}")
    private String storageLocation;
    
    @Override
    public Image store(MultipartFile file, String description) throws IOException {
        // Initialize storage location
        Path uploadPath = Paths.get(storageLocation);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Clean the filename
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        
        // Generate a unique filename to prevent overwrites
        String uniqueFilename = UUID.randomUUID() + "_" + filename;
        
        // Extract dimensions and other metadata
        String dimensions = "Unknown";
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(file.getBytes()));
            ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (directory != null) {
                if (directory.containsTag(ExifIFD0Directory.TAG_IMAGE_WIDTH) && 
                    directory.containsTag(ExifIFD0Directory.TAG_IMAGE_HEIGHT)) {
                    int width = directory.getInt(ExifIFD0Directory.TAG_IMAGE_WIDTH);
                    int height = directory.getInt(ExifIFD0Directory.TAG_IMAGE_HEIGHT);
                    dimensions = width + "x" + height;
                }
            }
        } catch (Exception e) {
            log.warn("Could not extract image metadata", e);
        }
        
        // Save file to filesystem
        Path destinationFile = uploadPath.resolve(Paths.get(uniqueFilename)).normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        
        // Create and save image metadata to database
        Image image = Image.builder()
                .name(filename)
                .originalFilename(filename)
                .contentType(file.getContentType())
                .size(file.getSize())
                .path(destinationFile.toString())
                .description(description)
                .dimensions(dimensions)
                .uploadedAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .data(null) // Not storing in DB for this implementation
                .build();
        
        return imageRepository.save(image);
    }
    
    @Override
    public Image getImage(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Image not found with id: " + id));
    }
    
    @Override
    public List<Image> getAllImages() {
        return imageRepository.findByOrderByUploadedAtDesc();
    }
    
    @Override
    public List<Image> searchImages(String keyword) {
        return imageRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    @Override
    public List<Image> getImagesByType(String contentType) {
        return imageRepository.findByContentType(contentType);
    }
    
    @Override
    public void deleteImage(Long id) {
        Image image = getImage(id);
        
        // Delete file from filesystem
        try {
            Files.deleteIfExists(Paths.get(image.getPath()));
        } catch (IOException e) {
            log.error("Error deleting file", e);
        }
        
        // Delete metadata from database
        imageRepository.deleteById(id);
    }
    
    @Override
    public Image updateImage(Long id, String description) throws IOException {
        Image image = getImage(id);
        
        // Update only allowed fields
        image.setDescription(description);
        image.setModifiedAt(LocalDateTime.now());
        
        return imageRepository.save(image);
    }
}
```

## Controller

### ImageController.java

```java
package com.example.imagestorage.controller;

import com.example.imagestorage.model.Image;
import com.example.imagestorage.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    
    @GetMapping("/")
    public String index(Model model) {
        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "index";
    }
    
    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload";
    }
    
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file,
                             @RequestParam(value = "description", required = false) String description,
                             RedirectAttributes redirectAttributes) {
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
                return "redirect:/upload";
            }
            
            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                redirectAttributes.addFlashAttribute("message", "Only image files are allowed");
                return "redirect:/upload";
            }
            
            Image savedImage = imageService.store(file, description);
            redirectAttributes.addFlashAttribute("message", 
                    "Image uploaded successfully: " + savedImage.getName());
            
            return "redirect:/";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", 
                    "Failed to upload image: " + e.getMessage());
            return "redirect:/upload";
        }
    }
    
    @GetMapping("/images/{id}")
    public String viewImage(@PathVariable Long id, Model model) {
        Image image = imageService.getImage(id);
        model.addAttribute("image", image);
        return "view";
    }
    
    @GetMapping("/images/{id}/view")
    @ResponseBody
    public ResponseEntity<Resource> serveImage(@PathVariable Long id) {
        Image image = imageService.getImage(id);
        Path path = Paths.get(image.getPath());
        Resource file = new FileSystemResource(path);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getOriginalFilename() + "\"")
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(file);
    }
    
    @GetMapping("/images/{id}/download")
    @ResponseBody
    public ResponseEntity<Resource> downloadImage(@PathVariable Long id) {
        Image image = imageService.getImage(id);
        Path path = Paths.get(image.getPath());
        Resource file = new FileSystemResource(path);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getOriginalFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
    
    @PostMapping("/images/{id}/delete")
    public String deleteImage(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        imageService.deleteImage(id);
        redirectAttributes.addFlashAttribute("message", "Image deleted successfully");
        return "redirect:/";
    }
    
    @GetMapping("/search")
    public String searchImages(@RequestParam String keyword, Model model) {
        List<Image> images = imageService.searchImages(keyword);
        model.addAttribute("images", images);
        model.addAttribute("keyword", keyword);
        return "index";
    }
    
    // REST API endpoints
    
    @GetMapping("/api/images")
    @ResponseBody
    public List<Image> getImages() {
        return imageService.getAllImages();
    }
    
    @GetMapping("/api/images/{id}")
    @ResponseBody
    public Image getImage(@PathVariable Long id) {
        return imageService.getImage(id);
    }
    
    @DeleteMapping("/api/images/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteImageApi(@PathVariable Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
```

## Exception Handling

### ImageNotFoundException.java

```java
package com.example.imagestorage.exception;

public class ImageNotFoundException extends RuntimeException {
    
    public ImageNotFoundException(String message) {
        super(message);
    }
    
    public ImageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### GlobalExceptionHandler.java

```java
package com.example.imagestorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleImageNotFoundException(ImageNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "File size exceeds maximum allowed upload size");
        
        return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

## Configuration

### StorageConfig.java

```java
package com.example.imagestorage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StorageConfig implements WebMvcConfigurer {

    @Value("${app.storage.location:uploads}")
    private String storageLocation;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(storageLocation);
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
```

## Properties Configuration

### application.properties

```properties
# Server configuration
server.port=8080

# Spring Data JPA and H2 configuration
spring.datasource.url=jdbc:h2:file:./data/imagedb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# File upload settings
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Application specific settings
app.storage.location=uploads
```

## HTML Templates

### index.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Image Storage System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .image-thumbnail {
            width: 200px;
            height: 150px;
            object-fit: cover;
            cursor: pointer;
            transition: transform 0.3s;
        }
        .image-thumbnail:hover {
            transform: scale(1.05);
        }
        .card {
            transition: box-shadow 0.3s;
        }
        .card:hover {
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <div class="row mb-4">
            <div class="col">
                <h1>Image Storage System</h1>
            </div>
            <div class="col-auto">
                <a href="/upload" class="btn btn-primary">Upload New Image</a>
            </div>
        </div>
        
        <!-- Flash message -->
        <div th:if="${message}" class="alert alert-success alert-dismissible fade show" role="alert">
            <span th:text="${message}"></span>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        
        <!-- Search -->
        <div class="row mb-4">
            <div class="col-md-6">
                <form action="/search" method="get" class="d-flex">
                    <input type="text" name="keyword" class="form-control me-2" placeholder="Search images..." 
                           th:value="${keyword}" required>
                    <button type="submit" class="btn btn-outline-primary">Search</button>
                </form>
            </div>
        </div>
        
        <!-- Images gallery -->
        <div class="row g-4">
            <div th:if="${#lists.isEmpty(images)}" class="col-12">
                <div class="alert alert-info">
                    No images found. <a href="/upload">Upload your first image</a>.
                </div>
            </div>
            
            <div th:each="image : ${images}" class="col-md-4 col-sm-6">
                <div class="card h-100">
                    <a th:href="@{/images/{id}(id=${image.id})}">
                        <img th:src="@{/images/{id}/view(id=${image.id})}" class="card-img-top image-thumbnail" 
                             th:alt="${image.name}">
                    </a>
                    <div class="card-body">
                        <h5 class="card-title text-truncate" th:text="${image.name}">Image name</h5>
                        <p class="card-text" th:if="${image.description}" th:text="${image.description}">Description</p>
                        <p class="card-text text-muted">
                            <small>
                                <span th:text="${image.contentType}">Type</span> | 
                                <span th:text="${image.dimensions}">Dimensions</span> | 
                                <span th:text="${#numbers.formatDecimal(image.size / 1024, 0, 2) + ' KB'}">Size</span>
                            </small>
                        </p>
                    </div>
                    <div class="card-footer">
                        <small class="text-muted" th:text="${#temporals.format(image.uploadedAt, 'yyyy-MM-dd HH:mm')}">
                            Uploaded date
                        </small>
                        <div class="float-end">
                            <a th:href="@{/images/{id}/download(id=${image.id})}" class="btn btn-sm btn-outline-secondary">
                                Download
                            </a>
                            <button class="btn btn-sm btn-outline-danger" 
                                    onclick="confirmDelete(this)" 
                                    th:data-id="${image.id}">
                                Delete
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Delete confirmation modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Confirm Deletion</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    Are you sure you want to delete this image? This action cannot be undone.
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <form id="deleteForm" method="post">
                        <button type="submit" class="btn btn-danger">Delete</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function confirmDelete(button) {
            const imageId = button.getAttribute('data-id');
            const form = document.getElementById('deleteForm');
            form.action = '/images/' + imageId + '/delete';
            
            const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
            modal.show();
        }
    </script>
</body>
</html>
```

### upload.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Upload Image</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .preview-container {
            max-width: 300px;
            max-height: 300px;
            overflow: hidden;
            margin: 20px auto;
            border: 1px dashed #ccc;
            padding: 10px;
            text-align: center;
            display: none;
        }
        #imagePreview {
            max-width: 100%;
            max-height: 280px;
        }
        .drop-zone {
            border: 2px dashed #ccc;
            border-radius: 5px;
            padding: 30px;
            text-align: center;
            cursor: pointer;
            transition: border 0.3s;
        }
        .drop-zone:hover, .drop-zone.dragover {
            border-color: #0d6efd;
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <div class="row mb-4">
            <div class="col">
                <h1>Upload New Image</h1>
                <a href="/" class="btn btn-outline-secondary">Back to Gallery</a>
            </div>
        </div>
        
        <!-- Flash message -->
        <div th:if="${message}" class="alert alert-warning alert-dismissible fade show" role="alert">
            <span th:text="${message}"></span>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        
        <div class="row">
            <div class="col-md-8 mx-auto">
                <div class="card">
                    <div class="card-body">
                        <form method="POST" action="/upload" enctype="multipart/form-data">
                            <div class="mb-3">
                                <div class="drop-zone" id="dropZone">
                                    <p class="mb-0">Drag & drop image here or click to browse</p>
                                    <input type="file" name="file" id="file" class="d-none" accept="image/*" required>
                                </div>
                                <div class="preview-container" id="previewContainer">
                                    <img id="imagePreview" src="#" alt="Preview">
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="description" class="form-label">Description (optional)</label>
                                <textarea class="form-control" id="description" name="description" rows="3"></textarea>
                            </div>
                            
                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary">Upload Image</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>