/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.wb02;

import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.Mtrlms;
import com.hongyou.abner.data.model.Mtrtyp;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.abner.data.model.VMtrlms;
import com.hongyou.abner.data.pojo.MtrlmsPojo;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.ResponseEntry;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

/**
 * 物料管理
 *
 * @author Hong Bo Lin
 */
@RestController
@RequestMapping("/wb02")
public class WB02 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(WB02.class);

    /**
     * 保存项目进度
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(
            @RequestBody final MtrlmsPojo pojo, final HttpServletRequest request
    ) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Mtrlms mtrlms = null; VMtrlms oldMtrlms = null;
            if (ObjectUtil.isNotNull(pojo.getId())) {
                mtrlms = this.db().mtrlms().get(pojo.getId());
                oldMtrlms = new VMtrlms().mtrlid(mtrlms.getMtrlid()).oneById();
            }
            String skuNo = pojo.getSkuNo();

            // 新增
            if (ObjectUtil.isNull(mtrlms)) {
                mtrlms = new Mtrlms();
                mtrlms.cmpnid(loginUser.getCmpnid()).
                        cretby(operatorBy).
                        crettm(currentTime);

                // 自动生成物料号
                if (StringUtil.isBlank(skuNo)) {
                    Mtrtyp mtrtyp = this.db().mtrtyp().get(pojo.getMaterialTypeId());
                    skuNo = this.getSkuNo(mtrtyp);

                    mtrtyp.srilsd(mtrtyp.getSrilsd() + 1);
                    this.db().mtrtyp().save(mtrtyp);
                }
            } else if (StringUtil.isBlank(skuNo)) {
                return ResponseEntry.builder().code(-1).message("请输入物料号").build();
            }

            // 检查是否已存在
            if (StringUtil.isNotBlank(skuNo) && !ObjectUtil.equal(skuNo, mtrlms.getSkuno())) {
                Mtrlms existed = this.db().mtrlms().getBySkuNo(loginUser.getCmpnid(), skuNo);
                if (ObjectUtil.isNotNull(existed)) {
                    return ResponseEntry.builder().code(-1).message("物料编号已存在").build();
                }
            }

            mtrlms.mttyid(pojo.getMaterialTypeId()).
                    skuno(skuNo).
                    skunam(pojo.getSkuName()).
                    mtrimg(pojo.getMaterialImage()).
                    model(pojo.getModel()).
                    pcsprc(pojo.getPurchasePrice()).
                    slsprc(pojo.getSalesPrice()).
                    mnstqt(pojo.getMinStockQty()).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().mtrlms().save(mtrlms);

            // 记录日志
            String action = oldMtrlms == null ? "新增" : "修改";
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(WB02.class.getSimpleName()).
                    name("物料管理").
                    action(action).
                    message(StringUtil.format("物料[{}]{}成功", mtrlms.getSkuno(), action)).
                    oldValue(oldMtrlms).
                    newValue(mtrlms).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("物料保存失败", e);
            throw new RestRuntimeException("物料保存失败");
        }
    }

    /**
     * 删除物料
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids, final HttpServletRequest request) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);

            for (Long id: ids) {
                Mtrlms mtrlms = this.db().mtrlms().get(id);
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(WB02.class.getSimpleName()).
                        name("物料管理").
                        action("删除").
                        message(StringUtil.format("物料[{}]删除成功", mtrlms.getSkunam())).
                        newValue(mtrlms).
                        build();
                this.eventLogManager.critical(event);
                this.db().mtrlms().delete(mtrlms);
            }
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("物料类型删除失败", e);
            throw new RestRuntimeException("物料类型已被使用");
        }
    }

    /**
     * 自动生成物料号
     */
    private String getSkuNo(final Mtrtyp mtrtyp) {
        // 需要补位的位数(总长度 - 序号头长度 - 种子长度)
        int length = mtrtyp.getSrllen() - (
                mtrtyp.getMtsrhd().length() + mtrtyp.getSrilsd().toString().length()
        );
        String skuNo = String.format("%-" + length + "s", "").replace(' ', '0');
        return mtrtyp.getMtsrhd() + skuNo + mtrtyp.getSrilsd();
    }
}
