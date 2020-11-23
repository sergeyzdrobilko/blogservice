package com.veeam.blogservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Account extends RepresentationModel<Account> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    @Getter @Setter private long id;

    @Column(unique=true)
    @NotBlank
    @Size(min=2, max=50)
    @Getter @Setter private String name;

    @Column
    @NotBlank
    @Setter private String password;
}
