package org.iqmanager.repository;

import org.iqmanager.models.SeoCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface SeoCategoriesDAO extends JpaRepository<SeoCategories, Long> {
    SeoCategories getByCategoriesId(long id);
}
