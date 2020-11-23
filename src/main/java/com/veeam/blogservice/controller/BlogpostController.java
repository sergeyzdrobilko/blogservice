package com.veeam.blogservice.controller;

import com.veeam.blogservice.model.Account;
import com.veeam.blogservice.model.Blogpost;
import com.veeam.blogservice.model.BlogpostCollection;
import com.veeam.blogservice.service.AccountService;
import com.veeam.blogservice.service.BlogpostService;
import lombok.NonNull;
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
public class BlogpostController {
    @Autowired
    AccountService accountService;

    @Autowired
    BlogpostService blogpostService;

    @RequestMapping(value=FULL_BLOGPOSTS_URL_TEMPLATE, method = RequestMethod.GET)
    public ResponseEntity<BlogpostCollection> getBlogposts(@PathVariable(ACCOUNT_ID_VAR) long accountId) {
        List<Blogpost> blogpostList = blogpostService.getBlogposts();
        BlogpostCollection bCol = new BlogpostCollection();
        blogpostList.stream().map( bp -> {
                if(!bp.hasLink(SELF_URL_TEMPLATE)){
                    bp.add(linkTo(methodOn(BlogpostController.class).getBlogpost(accountId, bp.getId())).withSelfRel());
                }
                if(!bp.hasLink(COMMENTS_URL_TEMPLATE)){
                    bp.add(linkTo(methodOn(CommentController.class).getComments(accountId, bp.getId())).withRel(COMMENTS_URL_TEMPLATE));
                }
                return bp;
            }
        ).collect(Collectors.toList());

        bCol.setBlogposts(blogpostList);
        bCol.add(linkTo(methodOn(BlogpostController.class).addBlogpost(accountId, null)).withRel(CREATE_URL_TEMPLATE));
        return new ResponseEntity<>(bCol, HttpStatus.OK);
    }

    @RequestMapping(value=FULL_BLOGPOSTS_URL_TEMPLATE, method = RequestMethod.POST)
    public ResponseEntity<Blogpost> addBlogpost(@PathVariable(ACCOUNT_ID_VAR) long accountId,
                                                @Valid @RequestBody Blogpost blogpost) {
        Account account = accountService.getAccount(accountId).orElse(null);
        if (account == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        blogpost.setAccount(account);
        blogpost = blogpostService.addBlogpost(blogpost);
        setupLinks(blogpost, accountId);
        blogpost.add(linkTo(methodOn(CommentController.class).getComments(accountId, blogpost.getId())).withRel(COMMENTS_URL_TEMPLATE));
        return new ResponseEntity<>(blogpost, HttpStatus.CREATED);
    }

    @RequestMapping(value=SINGLE_BLOGPOSTS_URL_TEMPLATE, method = RequestMethod.GET)
    public ResponseEntity<Blogpost> getBlogpost(@PathVariable(ACCOUNT_ID_VAR) long accountId,
                                                @PathVariable(ID_VAR) long id) {
        Blogpost blogpost = blogpostService.getBlogpost(id).orElse(null);
        if (blogpost == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        setupLinks(blogpost, accountId);
        blogpost.add(linkTo(methodOn(CommentController.class).addComment(accountId, blogpost.getId(), null)).withRel(ADD_COMMENT_URL_TEMPLATE));

        return new ResponseEntity<>(blogpost, HttpStatus.OK);
    }

    @RequestMapping(value=SINGLE_BLOGPOSTS_URL_TEMPLATE, method = RequestMethod.DELETE)
    public ResponseEntity removeBlogpost(@PathVariable(ACCOUNT_ID_VAR) long accountId,
                                                @PathVariable(ID_VAR) long id) {
        Blogpost blogpost = blogpostService.getBlogpost(id).orElse(null);
        if (blogpost == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (blogpost.getAccount().getId() == accountId && blogpost.getComments().size() == 0) {
            blogpostService.removeBlogpost(blogpost);
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }

    @RequestMapping(value=SINGLE_BLOGPOSTS_URL_TEMPLATE, method = RequestMethod.PUT)
    public ResponseEntity updateBlogpost(@PathVariable(ACCOUNT_ID_VAR) long accountId,
                                         @PathVariable(ID_VAR) long id,
                                         @Valid @RequestBody Blogpost blogpost) {
        @NonNull Blogpost existingBlogpost = blogpostService.getBlogpost(id).orElse(null);
        if (existingBlogpost == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        blogpost.setAccount(existingBlogpost.getAccount());
        if (existingBlogpost.getAccount().getId() == accountId && existingBlogpost.getComments().size() == 0) {
            blogpostService.updateBlogpost(blogpost);
            return new ResponseEntity<>(blogpost, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }

    private void setupLinks(Blogpost blogpost, Long accountId) {
        blogpost.add(linkTo(methodOn(BlogpostController.class).getBlogpost(accountId, blogpost.getId())).withSelfRel());
        if (blogpost.getAccount().getId() == accountId && (blogpost.getComments() == null || blogpost.getComments().size() == 0)) {
            blogpost.add(linkTo(methodOn(BlogpostController.class).updateBlogpost(accountId, blogpost.getId(), null)).withRel(UPDATE_URL_TEMPLATE));
            blogpost.add(linkTo(methodOn(BlogpostController.class).removeBlogpost(accountId, blogpost.getId())).withRel(REMOVE_URL_TEMPLATE));
        }
        blogpost.add(linkTo(methodOn(BlogpostController.class).getBlogposts(accountId)).withRel(BLOGPOSTS_URL_TEMPLATE));
    }
}
