package com.dailycodebuffer.springbootdemo.controller;

import com.dailycodebuffer.springbootdemo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {
    @RequestMapping("/")
    public String home(){
        return "Hello World";
    }

    @GetMapping("/user")
    public User getUser(){
        User user = new User();
        user.setId("1");
        user.setName("shoaib");
        user.setEmail("shoaib@gmail.com");
        return user;
    }
    @GetMapping("/user/{id}")
    public String pathVariable(@PathVariable("id")  String no){
//        User user = new User();
//        user.setId("1");
//        user.setName("shoaib");
//        user.setEmail("shoaib@gmail.com");
//        return user;
        return "Path variable value is: " + no;
    }
}
