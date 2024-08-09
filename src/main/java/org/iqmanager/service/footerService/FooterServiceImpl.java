package org.iqmanager.service.footerService;

import org.iqmanager.models.PhotoReport;
import org.iqmanager.models.Poster;
import org.iqmanager.repository.PhotoReportDAO;
import org.iqmanager.repository.PosterDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FooterServiceImpl implements FooterService {

    private final PosterDAO posterDAO;
    private final PhotoReportDAO photoReportDAO;

    public FooterServiceImpl(PosterDAO posterDAO, PhotoReportDAO photoReportDAO) {
        this.posterDAO = posterDAO;
        this.photoReportDAO = photoReportDAO;
    }

    @Override
    public List<Poster> getPosters() {
        return posterDAO.findAll();
    }

    @Override
    public List<PhotoReport> getPhotoReports() {
        return photoReportDAO.findAll();
    }
}
