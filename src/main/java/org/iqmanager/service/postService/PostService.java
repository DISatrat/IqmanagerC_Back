package org.iqmanager.service.postService;

import org.iqmanager.dto.PostDTO;
import org.iqmanager.dto.PostListDTO;
import org.iqmanager.dto.RequestDTO;
import org.iqmanager.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public interface PostService {
    PostDTO getPostDTO(long id, long idCategory, String language);
    List<PostListDTO> getPostsPagination(long id, String country, String region, byte minStars, byte maxStars, long minPrice, long maxPrice, int page, int quantity);
    long getQuantityPosts(long id, String country, String region, long minPrice, long maxPrice, byte minStars, byte maxStars);
    List<PostListDTO> getPostsForSearch(String request);
    Post getPost(long id);
    ArrayList<Long> getAllPostIdsByCategoryId(long id);
    ArrayList<Long> getAllPostIds();
    List<PostListDTO> getPostsPaginationOrderByPriceAsc(Category category, String country, String region, byte minStars, byte maxStars, long minPrice, long maxPrice, Pageable pageable);
    List<PostListDTO> getPostsPaginationOrderByPriceDesc(Category category, String country, String region, byte minStars, byte maxStars, long minPrice, long maxPrice, Pageable pageable);
    List<PostListDTO> getPostsPaginationOrderByStarsAsc(Category category, String country, String region, byte minStars, byte maxStars, long minPrice, long maxPrice, Pageable pageable);
    List<PostListDTO> getPostsPaginationOrderByStarsDesc(Category category, String country, String region, byte minStars, byte maxStars, long minPrice, long maxPrice, Pageable pageable);
    PerformerData getPerformerByPostId(long id);
    List<PostListDTO> getListPosts(Category category);

    Page<PostListDTO> filterPosts(Category category, long priceMin, long priceMax, Instant date, Pageable pageable);
    void updatePostViews(long id, long newViews);
    Post findPostById(long id);

    void addNewFeedback(long postId, String username, String feedbackText, byte stars);

    //Поиск
    List<PostListDTO> searchPostsByText(String text);

    void saveOrderRequest(RequestDTO requestDTO);
}
