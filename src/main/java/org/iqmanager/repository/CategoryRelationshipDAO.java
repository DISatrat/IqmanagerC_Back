package org.iqmanager.repository;

import org.iqmanager.models.Category;
import org.iqmanager.models.CategoryRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;

@RepositoryRestController
public interface CategoryRelationshipDAO extends JpaRepository<CategoryRelationship, Long> {
    List<CategoryRelationship> findCategoryRelationshipsByParentCategory(Category parentCategory);
    @Query("SELECT cr.childCategory FROM CategoryRelationship cr")
    List<Category> findAllChildCategoryIds();
    List<CategoryRelationship> findAllByParentCategory(Category parent_category);
}
