package com.mypackage.SpringStarter.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AController {
    @RequestMapping("/hello")
    public String requestMethodName() {
        return "Hello";
    }
    
}
