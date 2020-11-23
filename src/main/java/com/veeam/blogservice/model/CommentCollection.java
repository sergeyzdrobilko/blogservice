package com.veeam.blogservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class CommentCollection extends RepresentationModel<CommentCollection> {
    @Getter @Setter private List<Comment> comments;
}
