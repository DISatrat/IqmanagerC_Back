package org.iqmanager.repository;


import org.iqmanager.models.SeoPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;


//todo Если сео не заполнен, возращается статус ОК и "". Необходимо возвращать полную структуру данных + если данных для нее нет, хотя бы закидывать в title/description название и описание поста
@RepositoryRestController
public interface SeoPostsDAO extends JpaRepository<SeoPosts, Long> {
    SeoPosts getByPostId(long post_id);
}
