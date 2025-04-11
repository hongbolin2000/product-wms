/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.dn05;

import com.hongyou.abner.data.pojo.RpheadPojo;
import com.hongyou.abner.data.pojo.RplinePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Berlin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReceiptPojo extends RpheadPojo {

    /**
     * 销售物料
     */
    private List<RplinePojo> materials;
}