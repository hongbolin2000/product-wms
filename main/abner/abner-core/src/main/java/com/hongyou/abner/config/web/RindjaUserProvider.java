/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.config.web;

import cn.dev33.satoken.stp.StpUtil;
import com.hongyou.abner.data.model.Pmsnms;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.baron.RindjaUserDetail;
import com.hongyou.baron.RindjaUserLoader;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.hongyou.abner.data.model.table.PmsnmsTableDef.PMSNMS;
import static com.hongyou.abner.data.model.table.RolpmsTableDef.ROLPMS;
import static com.hongyou.abner.data.model.table.UsrrolTableDef.USRROL;

/**
 * 提供给Rindja模块用户信息
 *
 * @author Berlin
 */
@Component
public class RindjaUserProvider extends UserDataProvider implements RindjaUserLoader {

    /**
     * 加载用户信息
     */
    @Override
    public RindjaUserDetail loadLoginUser() {
        Userms loginUser = this.getLoginUser();
        return RindjaUserDetail.builder().
                companyId(loginUser.getCmpnid()).
                username(loginUser.getUsernm()).
                build();
    }

    /**
     * 加载用户权限
     *
     * @param module 模块号
     */
    @Override
    public List<String> loadUserPermissions(String module) {
        Long loginUserId = StpUtil.getLoginIdAsLong();

        // 用户权限
        QueryWrapper wrapper = QueryWrapper.create();
        wrapper.select(QueryMethods.distinct(PMSNMS.ACTCDE)).
                from(PMSNMS).
                innerJoin(ROLPMS).on(PMSNMS.PMSNID.eq(ROLPMS.PMSNID)).
                innerJoin(USRROL).on(ROLPMS.ROLEID.eq(USRROL.ROLEID)).
                where(USRROL.USERID.eq(loginUserId)).
                and(PMSNMS.PMSNCD.eq(module));
        List<Pmsnms> pmsnmss = this.db().pmsnms().list(wrapper);
        return pmsnmss.stream().map(Pmsnms::getActcde).toList();
    }
}
