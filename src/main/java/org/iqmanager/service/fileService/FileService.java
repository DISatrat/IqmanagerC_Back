package org.iqmanager.service.fileService;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String upload(MultipartFile file, String directoryPah);
    Resource download(String key, String directoryPah);
    void delete (String key, String directoryPah);
}
