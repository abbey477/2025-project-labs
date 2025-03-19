## Additional HTML Templates

### view.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:text="${image.name} + ' - Image Storage'">Image View</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .full-image {
            max-width: 100%;
            max-height: 70vh;
            margin: 0 auto;
            display: block;
        }
        .metadata {
            background-color: #f8f9fa;
            border-radius: 5px;
            padding: 15px;
        }
    </style>
</head>
<body>
    <div class="container my-4">
        <div class="row mb-4">
            <div class="col">
                <a href="/" class="btn btn-outline-secondary">← Back to Gallery</a>
            </div>
        </div>

        <div class="card mb-4">
            <div class="card-header">
                <h3 th:text="${image.name}">Image Name</h3>
            </div>
            <div class="card-body text-center">
                <img th:src="@{/images/{id}/view(id=${image.id})}" class="full-image" th:alt="${image.name}">
            </div>
        </div>
        
        <div class="row">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h5>Image Details</h5>
                    </div>
                    <div class="card-body">
                        <p th:if="${image.description}" th:text="${image.description}">Description</p>
                        <p th:unless="${image.description}" class="text-muted">No description provided.</p>
                        
                        <div class="metadata mt-4">
                            <div class="row">
                                <div class="col-md-6">
                                    <p><strong>File Name:</strong> <span th:text="${image.originalFilename}">filename.jpg</span></p>
                                    <p><strong>Content Type:</strong> <span th:text="${image.contentType}">image/jpeg</span></p>
                                    <p><strong>Dimensions:</strong> <span th:text="${image.dimensions}">1920x1080</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p><strong>Size:</strong> <span th:text="${#numbers.formatDecimal(image.size / 1024, 0, 2) + ' KB'}">123.45 KB</span></p>
                                    <p><strong>Uploaded:</strong> <span th:text="${#temporals.format(image.uploadedAt, 'yyyy-MM-dd HH:mm')}">2023-01-01</span></p>
                                    <p><strong>Modified:</strong> <span th:text="${#temporals.format(image.modifiedAt, 'yyyy-MM-dd HH:mm')}">2023-01-01</span></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header">
                        <h5>Actions</h5>
                    </div>
                    <div class="card-body">
                        <div class="d-grid gap-2">
                            <a th:href="@{/images/{id}/download(id=${image.id})}" class="btn btn-primary">
                                Download Image
                            </a>
                            <button type="button" class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#editModal">
                                Edit Description
                            </button>
                            <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal">
                                Delete Image
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Edit modal -->
    <div class="modal fade" id="editModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Description</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form th:action="@{/images/{id}/update(id=${image.id})}" method="post">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="description" class="form-label">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="3" 
                                      th:text="${image.description}"></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Save changes</button>
                    </div>
                </form>
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
                    <form th:action="@{/images/{id}/delete(id=${image.id})}" method="post">
                        <button type="submit" class="btn btn-danger">Delete</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
```

## Tests

### ImageControllerTest.java

```java
package com.example.imagestorage.controller;

import com.example.imagestorage.model.Image;
import com.example.imagestorage.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ImageController.class)
public class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    private Image testImage;
    private List<Image> testImages;

    @BeforeEach
    void setUp() {
        testImage = Image.builder()
                .id(1L)
                .name("test.jpg")
                .originalFilename("test.jpg")
                .contentType("image/jpeg")
                .size(1024L)
                .path("/tmp/test.jpg")
                .dimensions("800x600")
                .description("Test image")
                .uploadedAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        testImages = Arrays.asList(testImage);
    }

    @Test
    void testIndexPage() throws Exception {
        when(imageService.getAllImages()).thenReturn(testImages);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("images", testImages));
    }

    @Test
    void testUploadForm() throws Exception {
        mockMvc.perform(get("/upload"))
                .andExpect(status().isOk())
                .andExpect(view().name("upload"));
    }

    @Test
    void testUploadImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        when(imageService.store(any(), anyString())).thenReturn(testImage);

        mockMvc.perform(multipart("/upload")
                        .file(file)
                        .param("description", "Test description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testViewImage() throws Exception {
        when(imageService.getImage(anyLong())).thenReturn(testImage);

        mockMvc.perform(get("/images/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("view"))
                .andExpect(model().attribute("image", testImage));
    }

    @Test
    void testSearchImages() throws Exception {
        when(imageService.searchImages(anyString())).thenReturn(testImages);

        mockMvc.perform(get("/search")
                        .param("keyword", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("images", testImages))
                .andExpect(model().attribute("keyword", "test"));
    }

    @Test
    void testDeleteImage() throws Exception {
        doNothing().when(imageService).deleteImage(anyLong());

        mockMvc.perform(post("/images/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
```

### ImageServiceTest.java

```java
package com.example.imagestorage.service;

import com.example.imagestorage.exception.ImageNotFoundException;
import com.example.imagestorage.model.Image;
import com.example.imagestorage.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageServiceImpl imageService;

    @TempDir
    Path tempDir;

    private Image testImage;
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        // Set the storage location to the temporary directory
        ReflectionTestUtils.setField(imageService, "storageLocation", tempDir.toString());

        // Create a test image
        testImage = Image.builder()
                .id(1L)
                .name("test.jpg")
                .originalFilename("test.jpg")
                .contentType("image/jpeg")
                .size(1024L)
                .path(tempDir.resolve("test.jpg").toString())
                .dimensions("800x600")
                .description("Test image")
                .uploadedAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        // Create a mock multipart file
        multipartFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
    }

    @Test
    void testGetAllImages() {
        List<Image> images = Arrays.asList(testImage);
        when(imageRepository.findByOrderByUploadedAtDesc()).thenReturn(images);

        List<Image> result = imageService.getAllImages();

        assertEquals(1, result.size());
        assertEquals(testImage.getId(), result.get(0).getId());
        verify(imageRepository, times(1)).findByOrderByUploadedAtDesc();
    }

    @Test
    void testGetImage() {
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(testImage));

        Image result = imageService.getImage(1L);

        assertNotNull(result);
        assertEquals(testImage.getId(), result.getId());
        verify(imageRepository, times(1)).findById(1L);
    }

    @Test
    void testGetImageNotFound() {
        when(imageRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ImageNotFoundException.class, () -> {
            imageService.getImage(1L);
        });
        verify(imageRepository, times(1)).findById(1L);
    }

    @Test
    void testSearchImages() {
        List<Image> images = Arrays.asList(testImage);
        when(imageRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(images);

        List<Image> result = imageService.searchImages("test");

        assertEquals(1, result.size());
        assertEquals(testImage.getId(), result.get(0).getId());
        verify(imageRepository, times(1)).findByNameContainingIgnoreCase("test");
    }

    @Test
    void testGetImagesByType() {
        List<Image> images = Arrays.asList(testImage);
        when(imageRepository.findByContentType(anyString())).thenReturn(images);

        List<Image> result = imageService.getImagesByType("image/jpeg");

        assertEquals(1, result.size());
        assertEquals(testImage.getId(), result.get(0).getId());
        verify(imageRepository, times(1)).findByContentType("image/jpeg");
    }

    @Test
    void testStore() throws IOException {
        when(imageRepository.save(any(Image.class))).thenReturn(testImage);

        Image result = imageService.store(multipartFile, "Test description");

        assertNotNull(result);
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void testUpdateImage() throws IOException {
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(testImage));
        when(imageRepository.save(any(Image.class))).thenReturn(testImage);

        Image result = imageService.updateImage(1L, "Updated description");

        assertNotNull(result);
        assertEquals("Updated description", testImage.getDescription());
        verify(imageRepository, times(1)).findById(1L);
        verify(imageRepository, times(1)).save(testImage);
    }

    @Test
    void testDeleteImage() throws IOException {
        // Create a temporary file
        Path tempFile = tempDir.resolve("test.jpg");
        java.nio.file.Files.createFile(tempFile);
        
        testImage.setPath(tempFile.toString());
        
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(testImage));
        doNothing().when(imageRepository).deleteById(anyLong());

        imageService.deleteImage(1L);

        verify(imageRepository, times(1)).findById(1L);
        verify(imageRepository, times(1)).deleteById(1L);
        assertFalse(java.nio.file.Files.exists(tempFile));
    }
}
```

## Update Controller to Handle Edit Action

```java
// Add this method to ImageController.java

@PostMapping("/images/{id}/update")
public String updateImage(@PathVariable Long id, 
                         @RequestParam String description,
                         RedirectAttributes redirectAttributes) {
    try {
        Image updatedImage = imageService.updateImage(id, description);
        redirectAttributes.addFlashAttribute("message", "Image description updated successfully");
        return "redirect:/images/" + id;
    } catch (IOException e) {
        redirectAttributes.addFlashAttribute("message", 
                "Failed to update image: " + e.getMessage());
        return "redirect:/images/" + id;
    }
}
```

## Running The Application

To run the application:

1. Clone or download the project
2. Open a terminal and navigate to the project directory
3. Run: `mvn spring-boot:run`
4. Open your browser and go to `http://localhost:8080`

## Project Features

1. **Upload Images**
    - Upload image files with description
    - Support for drag and drop upload
    - File type validation
    - Image preview before upload

2. **View Images**
    - Gallery view of all uploaded images
    - Detailed view of individual images
    - Image metadata display (dimensions, size, type)

3. **Manage Images**
    - Update image descriptions
    - Download images
    - Delete images

4. **Search Functionality**
    - Search images by name
    - Filter by image type

5. **REST API**
    - Get all images
    - Get image by ID
    - Delete image

## Security Considerations

This is a basic implementation. For a production application, consider implementing:

1. User authentication and authorization
2. CSRF protection
3. Input validation and sanitization
4. Rate limiting for uploads
5. Virus/malware scanning for uploaded files
6. Secure file storage (e.g., using a CDN or cloud storage)

## Future Enhancements

1. **Admin Dashboard**
    - Monitor storage usage
    - Bulk operations

2. **Advanced Features**
    - Image editing (crop, resize, rotate)
    - Image categorization and tagging
    - AI-based image recognition and auto-tagging
    - Versioning and revision history

3. **Performance Optimizations**
    - Image compression
    - Thumbnail generation
    - Lazy loading
    - Caching