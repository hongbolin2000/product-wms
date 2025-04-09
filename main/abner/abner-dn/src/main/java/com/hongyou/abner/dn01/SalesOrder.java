/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.dn01;

import com.hongyou.abner.data.pojo.SolinePojo;
import com.hongyou.abner.data.pojo.SoheadPojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Kevin Qian
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SalesOrder extends SoheadPojo {

    /**
     * 销售物料
     */
    private List<SolinePojo> materials;
}
