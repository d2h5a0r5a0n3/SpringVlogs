package com.mypackage.SpringStarter.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mypackage.SpringStarter.models.Post;
import com.mypackage.SpringStarter.services.PostService;

@Controller
public class HomeController {
    @Autowired
    private PostService postService;
    @GetMapping("/")
    public String index(Model model){
        List<Post> posts=postService.getAll();
        model.addAttribute("posts", posts);
        return "index";
    }
    // @GetMapping("/editor")
    // public String editor(Model model){
    //     return "editor";
    // }
}
