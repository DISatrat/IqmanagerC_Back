package org.iqmanager.service.favoriteService;

import org.iqmanager.models.Post;
import org.iqmanager.models.UserData;
import org.iqmanager.service.favoriteService.FavoriteService;
import org.iqmanager.service.postService.PostService;
import org.iqmanager.service.userDataService.UserDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FavoriteServiceImpl implements FavoriteService {


    private final UserDataService userDataService;
    private final PostService postService;

    public FavoriteServiceImpl(UserDataService userDataService, PostService postService) {
        this.userDataService = userDataService;
        this.postService = postService;
    }

    /** Получение всех объявлений добавленных в избранное */
    @Override
    public List<Post> getAll() {
        UserData userData = userDataService.getLoginnedAccount();
        return userDataService.getUser(userData.getId()).getFavorites();
    }

    /** Очистить избранное */
    @Override
    public void deleteAll() {
        UserData userData = userDataService.getLoginnedAccount();
        userData.setFavorites(new ArrayList<>());
        userDataService.save(userData);
    }

    /** Удалить объявление из избранного */
    @Override
    public void delete(long id) {
        UserData userData = userDataService.getLoginnedAccount();
        Post post = postService.getPost(id);
        userData.deleteFromFavorites(post);
        userDataService.save(userData);
    }
}