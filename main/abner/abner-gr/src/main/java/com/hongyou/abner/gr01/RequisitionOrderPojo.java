/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.gr01;

import com.hongyou.abner.data.pojo.RqheadPojo;
import com.hongyou.abner.data.pojo.RqlinePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Berlin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RequisitionOrderPojo extends RqheadPojo {

    /**
     * 请购物料
     */
    private List<RqlinePojo> materials;
}
