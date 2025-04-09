/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.gr05;

import com.hongyou.abner.data.pojo.PyheadPojo;
import com.hongyou.abner.data.pojo.PylinePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Hong Bo Lin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentPojo extends PyheadPojo {

    /**
     * 采购物料
     */
    private List<PylinePojo> materials;
}