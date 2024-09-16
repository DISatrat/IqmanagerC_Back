package org.iqmanager.service.fileService;

import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service("fileServiceForPostsImpl")
public class FileServiceImpl implements FileService {

    @SneakyThrows
    @Override
    public String upload(MultipartFile multipartFile, String pathDirectory) {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String key = generateKey(multipartFile.getOriginalFilename());

            key = key + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

            Path path = Paths.get(pathDirectory, key);
            Path file = Files.createFile(path);
            try (FileOutputStream stream = new FileOutputStream(file.toString())) {
                stream.write(multipartFile.getBytes());
            }
            return key;
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    @SneakyThrows
    public Resource download(String key, String pathDirectory) {
        Path path = Paths.get(pathDirectory + key);
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            Path defaultPath = Paths.get(pathDirectory + "default.jpg");
            return new UrlResource(defaultPath.toUri());
        }
    }

    @Override
    @SneakyThrows
    @Transactional
    public void delete(String key, String pathDirectory) {
        Path path = Paths.get(pathDirectory + key);
        Files.delete(path);
    }

    private String generateKey(String name) {
        return DigestUtils.md5Hex(name + LocalDateTime.now());
    }

}
