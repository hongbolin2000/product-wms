/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.ba01;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hongyou.abner.data.DataProvider;
import com.hongyou.baron.web.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Hong Bo Lin
 */
@RestController
@RequestMapping("/ba01")
public class BA01 extends DataProvider {

    @PostMapping("/deleteBatch")
    public ResponseEntity deleteBatch(@RequestBody final List<Long> ids) {
        return ResponseEntity.SUCCESS;
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody final ObjectNode body) {
        return ResponseEntity.SUCCESS;
    }
}
