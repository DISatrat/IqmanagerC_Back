package org.iqmanager.service.favoriteService;

import org.iqmanager.models.Post;

import java.util.List;

public interface FavoriteService {
    List<Post> getAll();
    void deleteAll();
    void delete(long id);
}
