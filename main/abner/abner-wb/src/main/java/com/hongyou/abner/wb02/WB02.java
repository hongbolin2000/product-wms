/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.wb02;

import com.hongyou.abner.data.model.*;
import com.hongyou.abner.data.pojo.MtrlmsPojo;
import com.hongyou.abner.web.UserDataProvider;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.ResponseEntry;
import com.hongyou.baron.web.event.EventLog;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

import static com.hongyou.abner.data.model.table.PrdmtrTableDef.PRDMTR;

/**
 * 物料管理
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/wb02")
public class WB02 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(WB02.class);

    /**
     * 保存物料
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(@RequestBody final MtrlmsPojo pojo) {

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
            Mtrtyp mtrtyp = this.db().mtrtyp().get(pojo.getMaterialTypeId());

            // 新增
            if (ObjectUtil.isNull(mtrlms)) {
                mtrlms = new Mtrlms();
                mtrlms.cmpnid(loginUser.getCmpnid()).
                        cretby(operatorBy).
                        crettm(currentTime);

                // 自动生成物料号
                if (StringUtil.isBlank(skuNo)) {
                    skuNo = this.getSkuNo(mtrtyp);
                    mtrtyp.srilsd(mtrtyp.getSrilsd() + 1);
                    this.db().mtrtyp().save(mtrtyp);
                }
            } else if (StringUtil.isBlank(skuNo)) {
                return ResponseEntry.builder().code(-1).message("物料号不能为空").build();
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
                    mtrimg(StringUtil.blankToDefault(pojo.getMaterialImage(), mtrtyp.getMtrimg())).
                    model(pojo.getModel()).
                    pcsprc(pojo.getPurchasePrice()).
                    slsprc(pojo.getSalesPrice()).
                    mnstqt(pojo.getMinStockQty()).
                    remark(pojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().mtrlms().save(mtrlms);
            VMtrlms vMtrlms = new VMtrlms().mtrlid(mtrlms.getMtrlid()).oneById();

            // 记录日志
            String action = oldMtrlms == null ? "新增" : "修改";
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(WB02.class.getSimpleName()).
                    name("物料管理").
                    action(action).
                    message(StringUtil.format("物料[{}]{}成功", mtrlms.getSkunam(), action)).
                    oldValue(oldMtrlms).
                    newValue(vMtrlms).
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
    public ResponseEntry delete(@RequestBody final List<Long> ids) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);

            for (Long id: ids) {
                VMtrlms vMtrlms = new VMtrlms().mtrlid(id).oneById();
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(WB02.class.getSimpleName()).
                        name("物料管理").
                        action("删除").
                        message(StringUtil.format("物料[{}]删除成功", vMtrlms.getSkunam())).
                        newValue(vMtrlms).
                        build();
                this.eventLogManager.critical(event);
                this.db().mtrlms().delete(id);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("物料删除失败", e);
            throw new RestRuntimeException("物料已被使用");
        }
    }

    /**
     * 原料关联
     */
    @PostMapping("/rawAssign")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry roleAssign(@RequestBody final MaterialPojo pojo) {

        try {
            Mtrlms mtrlms = this.db().mtrlms().get(pojo.getId());
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 删除原料清单
            QueryWrapper wrapper = QueryWrapper.create();
            wrapper.where(PRDMTR.PDMTID.eq(mtrlms.getMtrlid()));
            this.db().prdmtr().deleteQuery(wrapper);

            pojo.getMaterials().forEach(prdmtrPojo -> {
                Mtrlms rawMtrlms = this.db().mtrlms().get(prdmtrPojo.getMaterialId());
                Prdmtr prdmtr = new Prdmtr();
                prdmtr.pdmtid(mtrlms.getMtrlid()).
                        mtrlid(rawMtrlms.getMtrlid()).
                        quanty(prdmtrPojo.getQuantity()).
                        ndpdip(prdmtrPojo.getNeedProductInput()).
                        remark(prdmtrPojo.getRemark()).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().prdmtr().save(prdmtr);
            });

            // 记录日志
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(WB02.class.getSimpleName()).
                    name("物料管理").
                    action("原料关联").
                    message(StringUtil.format("物料[{}]原料关联成功", mtrlms.getSkunam())).
                    build();
            this.eventLogManager.info(event);
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("原料关联失败", e);
            throw new RestRuntimeException("原料关联失败");
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
