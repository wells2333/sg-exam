package com.github.tangyi.exam.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/exam/test")
    public String test1() {
        return "test1";
    }

    @RequestMapping("/test")
    public String test2() {
        return "test2";
    }
}
