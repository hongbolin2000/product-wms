/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.config.web;

import cn.dev33.satoken.stp.StpUtil;
import com.hongyou.abner.data.model.Pmsnms;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.abner.data.model.Usrwrh;
import com.hongyou.baron.RindjaUserLoader;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hongyou.abner.data.model.table.PmsnmsTableDef.PMSNMS;
import static com.hongyou.abner.data.model.table.RolpmsTableDef.ROLPMS;
import static com.hongyou.abner.data.model.table.UsrrolTableDef.USRROL;
import static com.hongyou.abner.data.model.table.UsrwrhTableDef.USRWRH;

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
    public void loadUserVariables(final Map<String, Object> variables) {
        Userms loginUser = this.getLoginUser();

        // 查询用户仓库
        QueryWrapper wrapper = QueryWrapper.create();
        wrapper.select(USRWRH.WRHSID).
                where(USRWRH.USERID.eq(loginUser.getUserid()));
        List<Usrwrh> usrwrhs = this.db().usrwrh().list(wrapper);
        List<Long> warehouseIds = usrwrhs.stream().map(Usrwrh::getWrhsid).toList();

        variables.put("_companyId", loginUser.getCmpnid());
        variables.put("_username", loginUser.getUsernm());
        variables.put("_warehouseIds", warehouseIds);
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
        wrapper.select(QueryMethods.distinct(PMSNMS.PMSNCD, PMSNMS.ACTCDE)).
                from(PMSNMS).
                innerJoin(ROLPMS).on(PMSNMS.PMSNID.eq(ROLPMS.PMSNID)).
                innerJoin(USRROL).on(ROLPMS.ROLEID.eq(USRROL.ROLEID)).
                where(USRROL.USERID.eq(loginUserId)).
                and(PMSNMS.PMSNCD.eq(module));
        List<Pmsnms> pmsnmss = this.db().pmsnms().list(wrapper);

        List<String> moduleActions = pmsnmss.stream().map(i -> i.getPmsncd() + "@" + i.getActcde()).toList();
        List<String> actions = pmsnmss.stream().map(Pmsnms::getActcde).toList();

        List<String> permissions = new ArrayList<>();
        permissions.addAll(actions);
        permissions.addAll(moduleActions);
        return permissions;
    }
}
