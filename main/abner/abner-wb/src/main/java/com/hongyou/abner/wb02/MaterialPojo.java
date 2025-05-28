/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.wb02;

import com.hongyou.abner.data.pojo.PrdmtrPojo;
import lombok.Data;

import java.util.List;

/**
 * @author Berlin
 */
@Data
public class MaterialPojo {

    /**
     * 成品物料ID
     */
    private Long id;

    /**
     * 物料清单
     */
    private List<PrdmtrPojo> materials;
}