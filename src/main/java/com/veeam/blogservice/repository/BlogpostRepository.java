package com.veeam.blogservice.repository;


import com.veeam.blogservice.model.Blogpost;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BlogpostRepository extends PagingAndSortingRepository<Blogpost, Long>, CrudRepository<Blogpost, Long> {

    @Query("SELECT b FROM Blogpost b LEFT JOIN FETCH b.comments")
    List<Blogpost> findAll();

    List<Blogpost> findByAccountId(Long accountId);
}
