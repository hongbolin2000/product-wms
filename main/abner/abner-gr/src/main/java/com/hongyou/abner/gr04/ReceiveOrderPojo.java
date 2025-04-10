/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.gr04;

import com.hongyou.abner.data.pojo.RoheadPojo;
import com.hongyou.abner.data.pojo.RolinePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Berlin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReceiveOrderPojo extends RoheadPojo {

    /**
     * 采购物料
     */
    private List<RolinePojo> materials;
}
