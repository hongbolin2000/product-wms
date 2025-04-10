/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.gr08;

import com.hongyou.abner.data.pojo.RolinePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Hong Bo Lin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReceiveLinePojo extends RolinePojo {

    /**
     * 收货数量
     */
    private BigDecimal receiveQty;

    /**
     * 序列号
     */
    private String serialNo;

    /**
     * 存放货位
     */
    private Long binId;

    /**
     * 存放箱号
     */
    private String cartonNo;

    /**
     * 备注
     */
    private String remark;
}
