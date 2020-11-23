package com.veeam.blogservice.controller;

import com.veeam.blogservice.model.Comment;
import com.veeam.blogservice.model.CommentCollection;
import com.veeam.blogservice.service.AccountService;
import com.veeam.blogservice.service.BlogpostService;
import com.veeam.blogservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.veeam.blogservice.controller.constants.RestUriTemplates.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CommentController {
    @Autowired
    AccountService accountService;

    @Autowired
    BlogpostService blogpostService;

    @Autowired
    CommentService commentService;

    @RequestMapping(value=FULL_COMMENTS_URL_TEMPLATE, method = RequestMethod.GET)
    public ResponseEntity<CommentCollection> getComments(@PathVariable(ACCOUNT_ID_VAR) long accountId,
                                                          @PathVariable(BLOGPOST_ID_VAR) long blogpostId) {
        List<Comment> commentList = commentService.getComments(accountId, blogpostId);
        CommentCollection cCol = new CommentCollection();
        commentList.stream().map( c -> {
            if (!c.hasLink(SELF_URL_TEMPLATE));
            {
                c.add(linkTo(methodOn(CommentController.class).getComment(accountId, blogpostId, c.getId())).withSelfRel());
            }
            return c;
        }).collect(Collectors.toList());

        cCol.setComments(commentList);
        cCol.add(linkTo(methodOn(CommentController.class).addComment(accountId, blogpostId, null)).withRel(CREATE_URL_TEMPLATE));
        return new ResponseEntity<>(cCol, HttpStatus.OK);
    }

    @RequestMapping(value=FULL_COMMENTS_URL_TEMPLATE, method = RequestMethod.POST)
    public ResponseEntity<Comment> addComment(@PathVariable(ACCOUNT_ID_VAR) long accountId,
                                              @PathVariable(BLOGPOST_ID_VAR) long blogpostId,
                                              @Valid @RequestBody Comment comment) {
        comment.setAccountId(accountId);
        comment.setBlogpostId(blogpostId);
        comment = commentService.addComment(comment);
        setupLinks(comment, accountId, blogpostId);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @RequestMapping(value=SINGLE_COMMENTS_URL_TEMPLATE, method = RequestMethod.GET)
    public ResponseEntity<Comment> getComment(@PathVariable(ACCOUNT_ID_VAR) long accountId,
                                                @PathVariable(BLOGPOST_ID_VAR) long blogpostId,
                                                @PathVariable(ID_VAR) long id) {
        Comment comment = commentService.getComment(id).orElse(null);
        if (comment == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        setupLinks(comment, accountId, blogpostId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @RequestMapping(value=SINGLE_COMMENTS_URL_TEMPLATE, method = RequestMethod.DELETE)
    public ResponseEntity removeComment(@PathVariable(ACCOUNT_ID_VAR) long accountId,
                                        @PathVariable(BLOGPOST_ID_VAR) long blogpostId,
                                        @PathVariable(ID_VAR) long id) {
        Comment comment = commentService.getComment(id).orElse(null);
        if (comment == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (comment.getAccountId() == accountId && comment.getBlogpostId() == blogpostId) {
            commentService.removeComment(comment);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }

    @RequestMapping(value=SINGLE_COMMENTS_URL_TEMPLATE, method = RequestMethod.PUT)
    public ResponseEntity updateComment(@PathVariable(ACCOUNT_ID_VAR) long accountId,
                                        @PathVariable(BLOGPOST_ID_VAR) long blogpostId,
                                        @PathVariable(ID_VAR) long id,
                                        @Valid @RequestBody Comment comment) {
        Comment existingComment = commentService.getComment(id).orElse(null);
        if (existingComment == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        comment.setId(existingComment.getId());
        comment.setAccountId(existingComment.getAccountId());
        comment.setBlogpostId(existingComment.getBlogpostId());

        if (existingComment.getAccountId() == accountId && existingComment.getBlogpostId() == blogpostId) {
            commentService.updateComment(comment);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    private void setupLinks(Comment comment, Long accountId, Long blogpostId) {
        comment.add(linkTo(methodOn(CommentController.class).getComment(accountId, blogpostId, comment.getId())).withSelfRel());
        if (comment.getAccountId().equals(accountId) && comment.getBlogpostId().equals(blogpostId)) {
            comment.add(linkTo(methodOn(CommentController.class).updateComment(accountId, blogpostId, comment.getId(),null)).withRel(UPDATE_URL_TEMPLATE));
            comment.add(linkTo(methodOn(CommentController.class).removeComment(accountId, blogpostId, comment.getId())).withRel(REMOVE_URL_TEMPLATE));
        }
        comment.add(linkTo(methodOn(CommentController.class).getComments(accountId, blogpostId)).withRel(BLOGPOSTS_URL_TEMPLATE));
    }
}