package org.iqmanager.service.categoryService;

import org.iqmanager.dto.CategoryDTO;
import org.iqmanager.models.Category;
import org.iqmanager.models.CategoryName;
import org.iqmanager.models.CategoryRelationship;
import org.iqmanager.repository.CategoryDAO;
import org.iqmanager.repository.CategoryRelationshipDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDAO categoryDAO;
    private final CategoryRelationshipDAO categoryRelationshipDAO;

    @Autowired
    public CategoryServiceImpl(CategoryDAO categoryDAO, CategoryRelationshipDAO categoryRelationshipDAO) {
        this.categoryDAO = categoryDAO;
        this.categoryRelationshipDAO = categoryRelationshipDAO;
    }

    /**
     * Получение категории по id родителя
     */
    @Override
    public List<CategoryDTO> findList(long parent_id, String language) {
        List<Category> categories;

        if (parent_id == 0L) {
            categories = categoryDAO.findAllByIdParent(0);

            List<Category> childCategories = categoryRelationshipDAO.findAllChildCategoryIds();

            categories.removeAll(childCategories);

        } else {
            Category parentCategory = categoryDAO.findCategoryById(parent_id);

            if (parentCategory.getIdParent() == 0L) {
                List<CategoryRelationship> categoriesRelationship = categoryRelationshipDAO.findCategoryRelationshipsByParentCategory(parentCategory);
                categories = categoriesRelationship.stream()
                        .map(CategoryRelationship::getChildCategory)
                        .collect(Collectors.toList());
            } else {
                categories = Collections.emptyList();
            }
        }

        List<CategoryDTO> categoryDTOList = categories.stream()
                .map(x -> CategoryDTO.categoryToDTO(x, language))
                .collect(Collectors.toList());

        if (categoryDTOList.isEmpty()) {
            return categories.stream()
                    .map(x -> CategoryDTO.categoryToDTO(x, "en"))
                    .collect(Collectors.toList());
        }

        return categoryDTOList;
    }

    /**
     * Получение категории по id
     */
    @Override
    @Cacheable("categoryById")
    public Category find(long id) {
        return categoryDAO.findById(id).isPresent() ? categoryDAO.findById(id).get() : new Category();
    }

    @Override
    public List<CategoryDTO> findAll(String language) {
        List<Category> categories = categoryDAO.findAll();
        List<CategoryDTO> categoryDTOList = categories.stream().map(x -> CategoryDTO.categoryToDTO(x, language)).collect(Collectors.toList());
        if (!categoryDTOList.isEmpty()) {
            return categoryDTOList;
        } else {
            return categories.stream().map(x -> CategoryDTO.categoryToDTO(x, "en")).collect(Collectors.toList());
        }
    }

    @Override
    public String getNameCategory(long id, String language) {
        List<CategoryName> categoryNames = categoryDAO.getOne(id).getCategoryNames();
        Optional<String> name = categoryNames.stream().filter(x -> Objects.equals(x.getLanguage(), language)).map(CategoryName::getName).findFirst();
        return name.orElseGet(() -> categoryNames.stream().filter(x -> Objects.equals(x.getLanguage(), "en")).map(CategoryName::getName).findFirst().get());
    }

    @Override
    public String[][] getHistory(long idParent, String language) {
        int i = 0;
        String[][] preHistory = new String[20][2];
        while (true) {
            if (idParent != 0) {
                Category category = find(idParent);
                Optional<String> name = category.getCategoryNames().stream().filter(x -> Objects.equals(x.getLanguage(), language)).map(CategoryName::getName).findFirst();
                preHistory[i][0] = name.orElseGet(() -> category.getCategoryNames().stream().filter(x -> Objects.equals(x.getLanguage(), "en")).map(CategoryName::getName).findFirst().get());
                preHistory[i][1] = String.valueOf(category.getId());
                idParent = category.getIdParent();
                i++;
            } else {
                break;
            }
        }
        String[][] history = new String[i + 1][2];
        for (int n = 0; n < i + 1; n++) {
            for (int m = 0; m < 2; m++) {
                history[n][m] = preHistory[n][m];
            }
        }


        return history;
    }
}
