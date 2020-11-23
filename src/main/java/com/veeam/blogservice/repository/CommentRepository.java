package com.veeam.blogservice.repository;

import com.veeam.blogservice.model.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Long>, CrudRepository<Comment, Long> {
    List<Comment> findAllByAccountIdAndBlogpostId(Long accId, Long bpId);

    Optional<Comment> findById(Long id);
}
