package com.veeam.blogservice.service;

import com.veeam.blogservice.model.Account;
import com.veeam.blogservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> getAccount(Long id){
        return accountRepository.findById(id);
    }

    public Account getAccount(String  name){
        return accountRepository.findAccountByName(name);
    }

    public Account getAccount(String name, String password){
        return accountRepository.findByNameAndPassword(name, password);
    }

    public Iterable<Account> getAccounts(){
        return accountRepository.findAll();
    }
}
