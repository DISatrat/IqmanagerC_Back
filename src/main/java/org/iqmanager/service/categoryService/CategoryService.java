package org.iqmanager.service.categoryService;

import org.iqmanager.dto.CategoryDTO;
import org.iqmanager.models.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findList(long parent_id, String language);
    Category find(long id);
    List<CategoryDTO> findAll(String language);
    String getNameCategory(long id, String language);
    String[][] getHistory(long id, String language);

}
