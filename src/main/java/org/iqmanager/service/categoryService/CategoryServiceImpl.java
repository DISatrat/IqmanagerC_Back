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
//        List<Category> categories;
//
//        if (parent_id == 0L) {
//            categories = categoryDAO.findAllByIdParent(0);
//
//            List<Category> childCategories = categoryRelationshipDAO.findAllChildCategoryIds();
//
//            categories.removeAll(childCategories);
//
//        } else {
//            Category parentCategory = categoryDAO.findCategoryById(parent_id);
//
//            if (parentCategory.getIdParent() == 0L) {
//                List<CategoryRelationship> categoriesRelationship = categoryRelationshipDAO.findCategoryRelationshipsByParentCategory(parentCategory);
//                categories = categoriesRelationship.stream()
//                        .map(CategoryRelationship::getChildCategory)
//                        .collect(Collectors.toList());
//            } else {
//                categories = Collections.emptyList();
//            }
//        }
//
//        List<CategoryDTO> categoryDTOList = categories.stream()
//                .map(x -> CategoryDTO.categoryToDTO(x, language))
//                .collect(Collectors.toList());
//
//        if (categoryDTOList.isEmpty()) {
//            return categories.stream()
//                    .map(x -> CategoryDTO.categoryToDTO(x, "en"))
//                    .collect(Collectors.toList());
//        }
//
//        return categoryDTOList;
        List<Category> categories;

        if (parent_id == 0L) {
            categories = categoryDAO.findAllByIdParentAndHidden(0,false);

            List<Category> childCategories = categoryRelationshipDAO.findChildCategoriesWithMultipleParents();

            categories.removeAll(childCategories);

        } else {
            Category parentCategory = categoryDAO.findCategoryByIdAndHidden(parent_id,false);

//            if (parentCategory.getIdParent() == 0L) {
                List<CategoryRelationship> categoriesRelationship = categoryRelationshipDAO.findCategoryRelationshipsByParentCategory(parentCategory);
                categories = categoriesRelationship.stream()
                        .map(CategoryRelationship::getChildCategory)
                        .collect(Collectors.toList());
//            } else {
//                categories = Collections.emptyList();
//            }
        }

        List<CategoryDTO> categoryDTOList = categories.stream()
                .map(x -> CategoryDTO.categoryToDTO(x, "ru"))
                .collect(Collectors.toList());

        if (categoryDTOList.isEmpty()) {
            return categories.stream()
                    .map(x -> CategoryDTO.categoryToDTO(x, "en"))
                    .collect(Collectors.toList());
        }

        categoryDTOList.forEach(x -> {
            Category templateCategory = categoryRelationshipDAO.findTemplateByChildCategoryId(x.getId());
            if (templateCategory != null) {
                // Фильтруем список имен категорий по языку 'ru'
                Optional<CategoryName> templateNameOpt = templateCategory.getCategoryNames().stream()
                        .filter(name -> "ru".equals(name.getLanguage()))
                        .findFirst();

                // Если нашли имя, берем его, иначе ставим null
                String templateName = templateNameOpt.map(CategoryName::getName).orElse(null);
                x.setTemplate(templateName);
            } else {
                x.setTemplate(null); // Если шаблон не найден, установим null
            }
        });

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

        while (idParent != 0) {
            Category category = find(idParent);
            if (category == null) {
                throw new NoSuchElementException("Category with parent ID " + idParent + " not found.");
            }

            Optional<String> nameOptional = category.getCategoryNames().stream()
                    .filter(x -> Objects.equals(x.getLanguage(), language))
                    .map(CategoryName::getName)
                    .findFirst();

            String categoryName = nameOptional.orElseGet(() ->
                    category.getCategoryNames().stream()
                            .filter(x -> Objects.equals(x.getLanguage(), "ru"))
                            .map(CategoryName::getName)
                            .findFirst()
                            .orElseThrow(() -> new NoSuchElementException("No category name found for category ID " + category.getId()))
            );

            preHistory[i][0] = categoryName;
            preHistory[i][1] = String.valueOf(category.getId());
            idParent = category.getIdParent();
            i++;
        }

        String[][] history = new String[i + 1][2];
        for (int n = 0; n <= i; n++) {
            System.arraycopy(preHistory[n], 0, history[n], 0, 2);
        }

        return history;
    }

}
