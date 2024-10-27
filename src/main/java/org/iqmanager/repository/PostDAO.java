package org.iqmanager.repository;

import org.iqmanager.models.Category;
import org.iqmanager.models.Post;
import org.iqmanager.models.enum_models.PostStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;
import java.util.Set;

@RepositoryRestController
public interface PostDAO extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    long countByCategoriesAndCountryAndRegionAndStarsGreaterThanEqualAndStarsLessThanEqualAndPriceGreaterThanEqualAndPriceLessThanEqualAndStatus(
            Category category, String country, String region, byte minStars, byte maxStars, long minPrice, long maxPrice, PostStatus status);

    List<Post> findAllByCategoriesAndCountryAndRegion(Category category, String country, String region, byte minStars, byte maxStars, long minPrice, long maxPrice, Pageable pageable);

    // возрастание цены

    List<Post> findAllByCategoriesAndCountryAndRegionAndStarsGreaterThanEqualAndStarsLessThanEqualAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceAsc(Category category, String country, String region, byte minStars, byte maxStars, long minPrice, long maxPrice, Pageable pageable); //По возрастанию цены

    //убывание цены

    List<Post> findAllByCategoriesAndCountryAndRegionAndStarsGreaterThanEqualAndStarsLessThanEqualAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceDesc(Category category, String country, String region, byte minStars, byte maxStars, long minPrice, long maxPrice, Pageable pageable);//По убыванию цены

    // возрастание оценки
    List<Post> findAllByCategoriesAndCountryAndRegionAndStarsGreaterThanEqualAndStarsLessThanEqualAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByStarsAsc(Category category, String country, String region, byte minStars, byte maxStars, long minPrice, long maxPrice, Pageable pageable);//По возрастанию оценок

    //убывание оценки
    List<Post> findAllByCategoriesAndCountryAndRegionAndStarsGreaterThanEqualAndStarsLessThanEqualAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByStarsDesc(Category category, String country, String region, byte minStars, byte maxStars, long minPrice, long maxPrice, Pageable pageable);//По убыванию оценок

    List<Post> findByCategories(Category category);

    Post findPostById(long id);

    List<Post> findByNameContainingIgnoreCase(String keyword);
}
