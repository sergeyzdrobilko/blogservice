package com.veeam.blogservice.controller;

import com.veeam.blogservice.model.Account;
import com.veeam.blogservice.service.AccountService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.veeam.blogservice.controller.constants.RestUriTemplates.ID_VAR;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<Account> getAccount(@PathVariable(ID_VAR) long id) {
        @NonNull Account account = accountService.getAccount(id).orElse(null);
        account.add(linkTo(methodOn(AccountController.class).getAccount(id)).withSelfRel());
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

}
