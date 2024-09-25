/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
package com.hongyou.abner.operator;

import com.hongyou.abner.data.DataProvider;
import com.hongyou.abner.data.model.Csttrc;
import com.hongyou.abner.data.model.Unitms;
import com.mybatisflex.core.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Berlin
 */
@RestController
@RequestMapping("/")
public class OperatorControl extends DataProvider {

    private static final Logger logger = LoggerFactory.getLogger(OperatorControl.class);

    @RequestMapping("/dbtest")
    public List<Unitms> getByAccount() {
        List<Unitms> list = this.db().unitms().list(new QueryWrapper());
        logger.info(list.get(0).getCretby() + Csttrc.STATUS.PROCESSED);
        return new ArrayList<>();
    }
}
