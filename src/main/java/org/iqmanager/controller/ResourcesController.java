package org.iqmanager.controller;


import org.iqmanager.service.fileService.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import static org.iqmanager.ApplicationC.URL_WEB;

@Validated
@RestController
@RequestMapping(value = "/api",produces = MediaType.IMAGE_JPEG_VALUE)
@CrossOrigin(origins = URL_WEB)
public class ResourcesController {
    private final Logger logger = LoggerFactory.getLogger(ResourcesController.class);
    private final FileService fileService;

    @Value("${directory.path.image}")
    private String imageDirectoryPath;

    @Value("${directory.path.pdf}")
    private String pdfDirectoryPath;

    public ResourcesController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/downloadImage")
    public ResponseEntity<byte[]> downloadImage(@RequestParam("key") String key){
        try {
            return ResponseEntity.ok(fileService.download(key, imageDirectoryPath).getInputStream().readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Performer -> ResourcesController -> downloadImage ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadedImage(@RequestBody MultipartFile file){
        try {
            return ResponseEntity.ok(fileService.upload(file, imageDirectoryPath));
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Performer -> ResourcesController -> uploadedImage ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/downloadPDF")
    public ResponseEntity<byte[]> downloadPdf(@RequestParam("key") String key){
        try {
            return ResponseEntity.ok(fileService.download(key, pdfDirectoryPath).getInputStream().readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Performer -> ResourcesController -> downloadImage ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/uploadPDF")
    public ResponseEntity<String> uploadedPdf(@RequestBody MultipartFile file){
        try {
            return ResponseEntity.ok(fileService.upload(file, pdfDirectoryPath));
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Performer -> ResourcesController -> uploadedImage ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
