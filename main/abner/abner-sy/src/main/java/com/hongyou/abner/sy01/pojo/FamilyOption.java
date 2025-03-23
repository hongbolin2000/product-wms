/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.sy01.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Hong Bo Lin
 */
@Data
@Builder
@AllArgsConstructor
public class FamilyOption {

    /**
     * 导航组显示标签
     */
    private String familyLabel;

    /**
     * 导航组名称
     */
    private String familyName;
}
