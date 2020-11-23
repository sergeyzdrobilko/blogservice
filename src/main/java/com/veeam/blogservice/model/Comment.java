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

@Entity
public class Comment extends RepresentationModel<Comment> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    @Getter @Setter private long id;

    @Column
    @Getter @Setter private Long blogpostId;

    @Column
    @Getter @Setter private Long accountId;

    @Column
    @NotBlank
    @Getter @Setter private String comment;
}
