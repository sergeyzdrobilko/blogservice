package com.veeam.blogservice.service;

import com.veeam.blogservice.model.Comment;
import com.veeam.blogservice.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void removeComment(Comment comment) {
        commentRepository.delete(comment);
    }

    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Optional<Comment> getComment(Long id){
        return commentRepository.findById(id);
    }

    public List<Comment> getComments(Long accId, Long bpId){
        return commentRepository.findAllByAccountIdAndBlogpostId(accId, bpId);
    }
}
