package com.veeam.blogservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Blogpost extends RepresentationModel<Blogpost> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    @Getter @Setter private long id;

    @ManyToOne()
    @JoinColumn(name="account_id", referencedColumnName = "id")
    @Getter @Setter private Account account;

    @OneToMany(mappedBy = "blogpostId")
    @Getter @Setter private List<Comment> comments;

    @Column
    @NotBlank
    @Getter @Setter private String text;
}
