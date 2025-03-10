package org.iqmanager.service.postService;

import lombok.SneakyThrows;
import org.iqmanager.dto.PostDTO;
import org.iqmanager.dto.PostListDTO;
import org.iqmanager.dto.RequestDTO;
import org.iqmanager.models.*;
import org.iqmanager.models.Calendar;
import org.iqmanager.models.enum_models.CalendarStatus;
import org.iqmanager.models.enum_models.PostStatus;
import org.iqmanager.models.specification.PostSpecification;
import org.iqmanager.repository.*;
import org.iqmanager.service.calendarService.CalendarService;
import org.iqmanager.service.calendarService.CalendarServiceImpl;
import org.iqmanager.service.categoryService.CategoryService;
import org.iqmanager.util.LevenshteinDistance;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostDAO postDAO;
    private final CategoryDAO categoryDAO;
    private final CommentDAO commentDAO;
    private final CategoryService categoryService;
    private final RequestFormDAO requestFormDAO;
    private final ModelMapper modelMapper;
    private final CalendarService calendarService;
    private final CalendarDAO calendarDAO;


    @Autowired
    public PostServiceImpl(PostDAO postDAO, CategoryDAO categoryDAO, CommentDAO commentDAO, CategoryService categoryService, RequestFormDAO requestFormDAO, ModelMapper modelMapper, @Lazy CalendarService calendarService, CalendarDAO calendarDAO) {
        this.postDAO = postDAO;
        this.categoryDAO = categoryDAO;
        this.commentDAO = commentDAO;
        this.categoryService = categoryService;
        this.requestFormDAO = requestFormDAO;
        this.modelMapper = modelMapper;
        this.calendarService = calendarService;
        this.calendarDAO = calendarDAO;
    }

    @Override
    public List<PostListDTO> getListPosts(Category category) {
        List<Post> postListDTOS = postDAO.findByCategories(category);
        return postsToDTO(postListDTOS.stream().filter(x-> PostStatus.PUBLISHED.equals(x.getStatus())).collect(Collectors.toList()));
    }

    @Override
    public void updatePostViews(long id, long newViews) {
        Post post = postDAO.findById(id).orElseThrow(NullPointerException::new);
        post.setViews(newViews);
        postDAO.save(post);
    }

    @Override
    public Post findPostById(long id) {
        return postDAO.findPostById(id);
    }


    //  Добавление отзывов
    @Override
    public void addNewFeedback(long postId, String username, String feedbackText, byte stars) {
        Post post = postDAO.findPostById(postId);
        Comment newComment = new Comment(username, feedbackText, stars);
        newComment.setPost(post);
        newComment.setDate(Instant.now());
        post.getComments().add(newComment);
        commentDAO.save(newComment);
        post.setStars(updatePostStars(postId));
        postDAO.save(post);
    }

    // Обновление рейтинга
    private byte updatePostStars(long postId) {
        Post post = postDAO.findPostById(postId);

        List<Byte> commentsStars = commentDAO.findAllByPost(post).stream()
                .map(Comment::getStars)
                .collect(Collectors.toList());

        double averageStars = commentsStars.stream()
                .mapToInt(Byte::intValue)
                .average()
                .orElse(0.0);


        return (byte) Math.round(averageStars);
    }

    // реализация поиска
    @Override
    public List<PostListDTO> searchPostsByText(String text) {
        List<Post> postListDTOS = postDAO.findByNameOrIdContainingIgnoreCase(text);
        return postsToDTO(postListDTOS.stream().filter(x-> PostStatus.PUBLISHED.equals(x.getStatus())).collect(Collectors.toList()));
    }

    //1
    @Override
    public void saveOrderRequest(RequestDTO requestDTO) {
        RequestForm requestForm = modelMapper.map(requestDTO, RequestForm.class);
        requestFormDAO.save(requestForm);
    }

    /**
     * Получить количество постов по условию
     */
    @Override
//    @Cacheable("quantityPosts")
    public long getQuantityPosts(long id, String country, String region, long minPrice, long maxPrice, byte minStars, byte maxStars, Instant date) {
        Category category = categoryDAO.getOne(id);
        return postDAO.countByCategoriesAndCountryAndRegionAndStarsGreaterThanEqualAndStarsLessThanEqualAndPriceGreaterThanEqualAndPriceLessThanEqualAndStatus(
                category, country, region, minStars, maxStars, minPrice, maxPrice, PostStatus.PUBLISHED);
    }

    @SneakyThrows
    @Override
    public PostDTO getPostDTO(long id, long idCategory, String language) {
        Post post = postDAO.getOne(id);
        PostDTO postDTO = PostDTO.postToDto(post);
        if (idCategory != -1) {
            postDTO.setHistory(categoryService.getHistory(idCategory, language));
        } else {
            postDTO.setHistory(categoryService.getHistory(postDAO.getOne(postDTO.getId()).getCategories().stream().findFirst().get().getId(), language));
        }
        return postDTO;
    }

    /**
     * Объявления с пагинацией
     */
    @Override
    @Cacheable("getPosts")
    public List<PostListDTO> getPostsPagination(long id, String country, String region, byte minStars, byte maxStars, long minPrice, long maxPrice, Instant date, int page, int quantity) {

        List<Post> postListDTOS = postDAO.findAllByCategoriesAndCountryAndRegion(categoryDAO.getOne(id), country, region, minStars, maxStars, minPrice, maxPrice, PageRequest.of(page, quantity));
        List<Post> filteredPosts = postListDTOS.stream()
                .filter(x -> PostStatus.PUBLISHED.equals(x.getStatus()))
                .filter(post -> isPerformerAvailable(post.getPerformer(), date))
                .collect(Collectors.toList());

        return postsToDTO(filteredPosts);
    }

    @Override
    //@Cacheable("getAllPostsByCategoryId")
    public ArrayList<Long> getAllPostIdsByCategoryId(long id) {
        return postsToDTO(postDAO.findByCategories(categoryDAO.getOne(id))).stream().map(PostListDTO::getId).collect(Collectors.toCollection(ArrayList::new));
    }

    //todo проверить, все ли ок
    @Override
//    @Cacheable("allPostIds")
    public ArrayList<Long> getAllPostIds() {
        ArrayList<Long> data = postsToDTO(postDAO.findAll()).stream().filter(post ->PostStatus.PUBLISHED.equals(post.getStatus())).map(PostListDTO::getId).collect(Collectors.toCollection(ArrayList::new));
        return data;
    }

    /**
     * Объявление по поиску
     */
    @Override
    public List<PostListDTO> getPostsForSearch(String request) {
        List<Post> posts = postDAO.findAll();
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        levenshteinDistance.setData(posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus())).map(Post::getName).collect(Collectors.toList()));
        System.out.println(levenshteinDistance.search(request).size());
        System.out.println(levenshteinDistance.search(request));


        return postsToDTO(posts);
    }

    /**
     * Объявление по id
     */
    @Override
    public Post getPost(long id) {
        return postDAO.findById(id).get();
    }

    /**
     * Объявление с пагинацией по возрастанию цены
     */
    @Override
    public List<PostListDTO> getPostsPaginationOrderByPriceAsc(Category category, String country, String region, byte minStars, byte maxStars, long minPrice, long maxPrice, Instant date, Pageable pageable) {
        List<Post> postListDTOS = postDAO.findAllByCategoriesAndCountryAndRegionAndStarsGreaterThanEqualAndStarsLessThanEqualAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceAsc(
                category, country, region, minStars, maxStars, minPrice, maxPrice, pageable);

        List<Post> filteredPosts = postListDTOS.stream()
                .filter(x -> PostStatus.PUBLISHED.equals(x.getStatus()))
                .filter(post -> isPerformerAvailable(post.getPerformer(), date))
                .collect(Collectors.toList());

        return postsToDTO(filteredPosts);
    }

    @Override
    public List<PostListDTO> getPostsPaginationOrderByPriceDesc(Category category, String country, String region, byte minStars, byte maxStars, long minPrice, long maxPrice, Instant date, Pageable pageable) {
        List<Post> postListDTOS = postDAO.findAllByCategoriesAndCountryAndRegionAndStarsGreaterThanEqualAndStarsLessThanEqualAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceDesc(
                category, country, region, minStars, maxStars, minPrice, maxPrice, pageable);

        List<Post> filteredPosts = postListDTOS.stream()
                .filter(x -> PostStatus.PUBLISHED.equals(x.getStatus()))
                .filter(post -> isPerformerAvailable(post.getPerformer(), date))
                .collect(Collectors.toList());

        return postsToDTO(filteredPosts);
    }

    @Override
    public List<PostListDTO> getPostsPaginationOrderByStarsAsc(Category category, String country, String region, byte minStars, byte maxStars, long minPrice, long maxPrice, Instant date, Pageable pageable) {
        List<Post> postListDTOS = postDAO.findAllByCategoriesAndCountryAndRegionAndStarsGreaterThanEqualAndStarsLessThanEqualAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByStarsAsc(
                category, country, region, minStars, maxStars, minPrice, maxPrice, pageable);

        List<Post> filteredPosts = postListDTOS.stream()
                .filter(x -> PostStatus.PUBLISHED.equals(x.getStatus()))
                .filter(post -> isPerformerAvailable(post.getPerformer(), date))
                .collect(Collectors.toList());

        return postsToDTO(filteredPosts);
    }

    @Override
    public List<PostListDTO> getPostsPaginationOrderByStarsDesc(Category category, String country, String region, byte minStars, byte maxStars, long minPrice, long maxPrice, Instant date, Pageable pageable) {
        List<Post> postListDTOS = postDAO.findAllByCategoriesAndCountryAndRegionAndStarsGreaterThanEqualAndStarsLessThanEqualAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByStarsDesc(
                category, country, region, minStars, maxStars, minPrice, maxPrice, pageable);

        List<Post> filteredPosts = postListDTOS.stream()
                .filter(x -> PostStatus.PUBLISHED.equals(x.getStatus()))
                .filter(post -> isPerformerAvailable(post.getPerformer(), date))
                .collect(Collectors.toList());

        return postsToDTO(filteredPosts);
    }

    @Override
    public Page<PostListDTO> filterPosts(String category, Long priceMin, Long priceMax, Instant date, Pageable pageable) {
        PostSpecification spec = new PostSpecification(category, priceMin, priceMax);
        Page<Post> postPage = postDAO.findAll(spec, pageable);

        List<Post> filteredPosts = postPage.getContent().stream()
                .filter(post -> isPerformerAvailable(post.getPerformer(), date))
                .collect(Collectors.toList());

        return new PageImpl<>(
                filteredPosts.stream()
                        .map(post -> modelMapper.map(post, PostListDTO.class))
                        .collect(Collectors.toList()),
                pageable,
                postPage.getTotalElements()
        );
    }


    private boolean isPerformerAvailable(PerformerData performer, Instant date) {
        if (date == null) {
            return true;
        }
        List<Calendar> calendars = calendarDAO.getAllByPerformer(performer);
        return calendars.stream().noneMatch(calendar ->
                (calendar.getStatus() == CalendarStatus.BUSY || calendar.getStatus() == CalendarStatus.DAY_OFF) &&
                        !date.isBefore(calendar.getBeginDate()) && !date.isAfter(calendar.getEndDate()));
    }
    /**
     * Получить исполнителя по объявлению
     */
    @Override
    public PerformerData getPerformerByPostId(long id) {
        return postDAO.getOne(id).getPerformer();
    }

    private List<PostListDTO> postsToDTO(List<Post> posts) {
        List<PostListDTO> postListMappers = new ArrayList<>();
        posts.forEach(x -> {
            postListMappers.add(
                    new PostListDTO(
                            x.getId(),
                            x.getName(),
                            x.getTitle(),
                            x.getZipImageKey(),
                            x.getBlurImage(),
                            x.getAddress(),
                            x.getViews(),
                            x.isLike(),
                            x.getStars(),
                            x.getPrice(),
                            x.getCurrency()));

        });
        return postListMappers;
    }
}
