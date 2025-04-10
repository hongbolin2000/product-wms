/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.ba01;

import com.hongyou.abner.data.pojo.SplctsPojo;
import com.hongyou.abner.data.pojo.SuplmsPojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Berlin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierPojo extends SuplmsPojo {

    /**
     * 供应商联系方式
     */
    private List<SplctsPojo> contactLines;
}
