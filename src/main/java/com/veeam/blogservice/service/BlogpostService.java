package com.veeam.blogservice.service;

import com.veeam.blogservice.model.Blogpost;
import com.veeam.blogservice.repository.BlogpostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogpostService {
    @Autowired
    BlogpostRepository blogpostRepository;

    public Blogpost addBlogpost(Blogpost blogpost) {
        return blogpostRepository.save(blogpost);
    }

    public Optional<Blogpost> getBlogpost(Long id){
        return blogpostRepository.findById(id);
    }

    public void removeBlogpost(Blogpost blogpost){
        blogpostRepository.delete(blogpost);
    }

    public Blogpost updateBlogpost(Blogpost blogpost){
        return blogpostRepository.save(blogpost);
    }

    public List<Blogpost> getBlogposts(){
        return blogpostRepository.findAll();
    }
}
