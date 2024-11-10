package org.iqmanager.repository;

import org.iqmanager.models.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;

@RepositoryRestController
public interface CategoryDAO extends JpaRepository<Category, Long> {
    List <Category> findAllByIdParent(long parent_id);

    List<Category> findAllByIdParentAndHidden(long parent_id, boolean hidden);
    List<Category> findAllByHidden(boolean hidden);
    Category findCategoryByIdAndHidden(long id, boolean hidden);
}
