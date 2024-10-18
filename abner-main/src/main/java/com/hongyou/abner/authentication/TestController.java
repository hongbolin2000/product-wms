/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.authentication;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Berlin
 */
@RestController
public class TestController {

    @RequestMapping("hello")
    public String hello() {
        return "Hello";
    }
}
