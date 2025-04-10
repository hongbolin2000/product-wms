/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.dn03;

import com.hongyou.abner.data.pojo.DoheadPojo;
import com.hongyou.abner.data.pojo.DolinePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Berlin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeliverOrderPojo extends DoheadPojo {

    /**
     * 发货物料
     */
    private List<DolinePojo> materials;
}
