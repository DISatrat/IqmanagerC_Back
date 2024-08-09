package org.iqmanager.repository;

import org.iqmanager.models.Category;
import org.iqmanager.models.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;

@RepositoryRestController
public interface CategoryNamesDAO extends JpaRepository<CategoryName, Long> {
//    List<Category> findAllByIdParent(long parent_id);
}
