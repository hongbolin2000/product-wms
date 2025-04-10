/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.sy02;

import com.hongyou.abner.data.pojo.RolemsPojo;
import lombok.Data;

import java.util.List;

/**
 * @author Berlin
 */
@Data
public class UserRolePojo {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 分配的角色
     */
    private List<RolemsPojo> roles;
}
