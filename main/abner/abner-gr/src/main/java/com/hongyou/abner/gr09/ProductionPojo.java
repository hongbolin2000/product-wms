/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.gr09;

import com.hongyou.abner.data.pojo.PdheadPojo;
import com.hongyou.abner.data.pojo.PdlinePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Berlin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductionPojo extends PdheadPojo {

    /**
     * 生产记录行
     */
    private List<PdlinePojo> productionLines;
}
