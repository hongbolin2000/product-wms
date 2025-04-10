/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.gr07;

import com.hongyou.abner.data.pojo.RoheadPojo;
import com.hongyou.abner.data.pojo.RolinePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Hong Bo Lin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReceiveOrderPojo extends RoheadPojo {

    /**
     * 请购物料
     */
    private List<RolinePojo> materials;
}
