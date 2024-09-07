package org.iqmanager.service.footerService;

import org.iqmanager.models.Banner;
import org.iqmanager.models.PhotoReport;

import java.util.List;

public interface FooterService {
    List<Banner> getPosters();
    List<PhotoReport> getPhotoReports();
}
