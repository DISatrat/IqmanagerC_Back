package org.iqmanager.service.seoService;

import org.iqmanager.models.SeoCategories;
import org.iqmanager.models.SeoPosts;
import org.iqmanager.repository.SeoCategoriesDAO;
import org.iqmanager.repository.SeoPostsDAO;
import org.iqmanager.service.seoService.SeoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SeoServiceImpl implements SeoService {
    private final SeoCategoriesDAO seoCategoriesDAO;
    private final SeoPostsDAO seoPostsDAO;

    public SeoServiceImpl(SeoCategoriesDAO seoCategoriesDAO, SeoPostsDAO seoPostsDAO) {
        this.seoCategoriesDAO = seoCategoriesDAO;
        this.seoPostsDAO = seoPostsDAO;
    }
    /** SEO к объявлению */
    @Override
    public SeoPosts getSeoPosts(long id) {
        return seoPostsDAO.getByPostId(id);
    }

    /** SEO к категориям */
    @Override
    public SeoCategories getSeoCategories(long id){
        return seoCategoriesDAO.getByCategoriesId(id);
    }
}
