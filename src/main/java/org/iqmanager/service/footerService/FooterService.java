package org.iqmanager.service.footerService;

import org.iqmanager.models.PhotoReport;
import org.iqmanager.models.Poster;

import java.util.List;

public interface FooterService {
    List<Poster> getPosters();
    List<PhotoReport> getPhotoReports();
}
