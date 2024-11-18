package org.iqmanager.repository;

import org.iqmanager.models.Comment;
import org.iqmanager.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;


@RepositoryRestController
public interface CommentDAO extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);
}
