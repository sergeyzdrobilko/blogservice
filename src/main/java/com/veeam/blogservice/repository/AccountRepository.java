package com.veeam.blogservice.repository;

import com.veeam.blogservice.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

//@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface AccountRepository extends PagingAndSortingRepository<Account, Long>, CrudRepository<Account, Long> {

    List<Account> findAll();

    Account findAccountByName(String name);

    Optional<Account> findById(Long id);

    Account findByNameAndPassword(String name, String password);

}