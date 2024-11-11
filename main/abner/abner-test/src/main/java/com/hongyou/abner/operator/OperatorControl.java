/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.operator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Berlin
 */
@RestController
@RequestMapping("/")
public class OperatorControl {

    private static final Logger logger = LoggerFactory.getLogger(OperatorControl.class);

    @RequestMapping("/dbtest")
    public String getByAccount() {
        return "hello";
    }
}
