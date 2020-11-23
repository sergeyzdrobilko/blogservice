package com.veeam.blogservice.controller;

import com.veeam.blogservice.model.Account;
import com.veeam.blogservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.veeam.blogservice.controller.constants.RestUriTemplates.BLOGPOSTS_URL_TEMPLATE;
import static com.veeam.blogservice.controller.constants.RestUriTemplates.LOGIN_URL_TEMPLATE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    AccountService accountService;

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity<String> serviceInfo() {
        return ResponseEntity.status(HttpStatus.OK).body("Blog Service Example");
    }

    @RequestMapping(value=LOGIN_URL_TEMPLATE, method = RequestMethod.GET)
    public HttpEntity login(@RequestParam String name,
                                     @RequestParam String password) {
        Account account = accountService.getAccount(name, password);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account with such name doesn't exist.");
        }
        setupLinks(account);
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @RequestMapping(value=LOGIN_URL_TEMPLATE, method = RequestMethod.POST)
    public ResponseEntity register(@Valid @RequestBody Account account) {
        Account existingAccount = accountService.getAccount(account.getName());
        if (existingAccount != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Account already exists.");
        }

        account = accountService.addAccount(account);
        setupLinks(account);
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    private void setupLinks(Account account) {
        account.add(linkTo(methodOn(AccountController.class).getAccount(account.getId())).withSelfRel());
        account.add(linkTo(methodOn(BlogpostController.class).getBlogposts(account.getId())).withRel(BLOGPOSTS_URL_TEMPLATE));
    }
}
