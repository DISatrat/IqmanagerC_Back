package org.iqmanager.service.seoService;


import org.iqmanager.models.SeoCategories;
import org.iqmanager.models.SeoPosts;

public interface SeoService {
    SeoPosts getSeoPosts(long id);
    SeoCategories getSeoCategories(long id);
}
