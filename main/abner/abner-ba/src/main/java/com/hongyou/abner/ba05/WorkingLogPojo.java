/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.ba05;

import com.hongyou.abner.data.pojo.WklghdPojo;
import com.hongyou.abner.data.pojo.WklglnPojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Berlin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkingLogPojo extends WklghdPojo {

    /**
     * 工作日志明细
     */
    private List<WklglnPojo> logLines;
}
