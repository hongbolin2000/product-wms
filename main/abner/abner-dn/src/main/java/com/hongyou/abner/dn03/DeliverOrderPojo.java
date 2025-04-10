/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.dn03;

import com.hongyou.abner.data.pojo.PoheadPojo;
import com.hongyou.abner.data.pojo.PolinePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Hong Bo Lin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeliverOrderPojo extends PoheadPojo {

    /**
     * 采购物料
     */
    private List<PolinePojo> materials;
}
