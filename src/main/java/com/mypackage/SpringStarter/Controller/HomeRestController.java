package com.mypackage.SpringStarter.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mypackage.SpringStarter.models.Post;
import com.mypackage.SpringStarter.services.PostService;
@RestController
@RequestMapping("/api/v1")
public class HomeRestController {
    @Autowired
    private PostService postService;

    Logger logger=LoggerFactory.getLogger(HomeRestController.class);
    @GetMapping("/")
    public List<Post> index(Model model){
        logger.debug("Error");
        return postService.getAll();
    }
}
