/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.sy02;

import com.hongyou.abner.data.pojo.RolemsPojo;
import com.hongyou.abner.data.pojo.WrhsmsPojo;
import lombok.Data;

import java.util.List;

/**
 * @author Berlin
 */
@Data
public class UserWarehousePojo {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 分配的仓库
     */
    private List<WrhsmsPojo> warehouses;
}
