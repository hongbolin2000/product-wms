/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.gr02;

import com.hongyou.abner.data.pojo.PoheadPojo;
import com.hongyou.abner.data.pojo.RqlinePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Hong Bo Lin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurchasingOrderPojo extends PoheadPojo {

    /**
     * 请购物料
     */
    private List<RqlinePojo> materials;
}
