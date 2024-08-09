package org.iqmanager.repository;

import org.iqmanager.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;


@RepositoryRestController
public interface CommentDAO extends JpaRepository<Comment, Long> {

}
