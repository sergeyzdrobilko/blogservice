package com.veeam.blogservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class BlogpostCollection extends RepresentationModel<BlogpostCollection> {
    @Getter @Setter private List<Blogpost> blogposts;
}
