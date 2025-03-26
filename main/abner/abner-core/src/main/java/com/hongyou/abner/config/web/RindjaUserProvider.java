/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.config.web;

import cn.dev33.satoken.stp.StpUtil;
import com.hongyou.abner.data.DataProvider;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.baron.RindjaUserDetail;
import com.hongyou.baron.RindjaUserLoader;
import org.springframework.stereotype.Component;

/**
 * 提供给Rindja模块用户信息
 *
 * @author Berlin
 */
@Component
public class RindjaUserProvider extends DataProvider implements RindjaUserLoader {

    /**
     * 加载用户信息
     */
    @Override
    public RindjaUserDetail loadLoginUser() {
        long userid = StpUtil.getLoginIdAsLong();
        Userms userms = this.db().userms().get(userid);
        return RindjaUserDetail.builder().
                companyId(userms.getCmpnid()).
                username(userms.getUsernm()).
                build();
    }
}
