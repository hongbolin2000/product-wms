/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.dn04;

import com.hongyou.abner.data.pojo.RolinePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Berlin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeliverLinePojo extends RolinePojo {

    /**
     * 发货数量
     */
    private BigDecimal deliverQty;

    /**
     * 序列号
     */
    private String serialNo;

    /**
     * 存放货位
     */
    private String binCode;

    /**
     * 备注
     */
    private String remark;
}
