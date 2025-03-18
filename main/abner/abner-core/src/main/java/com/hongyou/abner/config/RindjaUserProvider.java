/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.config;

import cn.dev33.satoken.stp.StpUtil;
import com.hongyou.abner.data.DataProvider;
import com.hongyou.abner.data.model.Oprtms;
import com.hongyou.baron.RindjaUserDetail;
import com.hongyou.baron.RindjaUserLoader;
import com.hongyou.baron.cache.CacheUtil;
import com.hongyou.baron.cache.TimedCache;
import org.springframework.stereotype.Service;

/**
 * 提供给Rindja模块用户信息
 *
 * @author Hong Bo Lin
 */
@Service
public class RindjaUserProvider extends DataProvider implements RindjaUserLoader {

    /**
     * 缓存7天内用户(7天未使用自动清除)
     */
    private final TimedCache<Long, RindjaUserDetail> userCaches = CacheUtil.newTimedCache(1000 * 60 * 60 * 24 * 7);

    /**
     * 加载用户信息
     */
    @Override
    public RindjaUserDetail load() {
        // 将登录用户进行缓存，避免每次从数据库拿取
        long userid = StpUtil.getLoginIdAsLong();
        if (!this.userCaches.containsKey(userid)) {
            Oprtms oprtms = this.db().oprtms().get(userid);
            RindjaUserDetail userDetail = RindjaUserDetail.builder().
                    companyId(oprtms.getCmpnid()).
                    username(oprtms.getAccunt()).
                    build();
            this.userCaches.put(userid, userDetail);
        }
        return this.userCaches.get(userid);
    }
}
