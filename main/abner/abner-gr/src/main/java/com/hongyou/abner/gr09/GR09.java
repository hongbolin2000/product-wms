/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.gr09;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hongyou.abner.data.model.*;
import com.hongyou.abner.data.pojo.PdlinePojo;
import com.hongyou.abner.web.UserDataProvider;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.JsonUtil;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.ResponseEntry;
import com.hongyou.baron.web.event.EventLog;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

import static com.hongyou.abner.data.model.table.PdlineTableDef.PDLINE;
import static com.hongyou.abner.data.model.table.VPrdmtrTableDef.VPRDMTR;

/**
 * 生产记录管理
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/gr09")
public class GR09 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(GR09.class);

    /**
     * 查询成品关联原料
     */
    @GetMapping("/getRawMaterials/{materialId}")
    public ResponseEntry getRawMaterials(@PathVariable("materialId") final long materialId) {

        try {
            List<VPrdmtr> vPrdmtrs = new VPrdmtr().
                    select(VPRDMTR.MTRLID, VPRDMTR.SKUNAM).
                    where(VPRDMTR.PDMTID.eq(materialId)).
                    and(VPRDMTR.NDPDIP.eq(Prdmtr.NDPDIP.Yes)).
                    list();
            ArrayNode data = JsonUtil.createArrayNode();
            for (VPrdmtr vPrdmtr : vPrdmtrs) {
                data.add(JsonUtil.createObjectNode().
                        put("materialId", vPrdmtr.getMtrlid().toString()).
                        put("skuName", vPrdmtr.getSkunam())
                );
            }
            return ResponseEntry.builder().body(data).build();
        } catch (Exception e) {
            logger.error("查询成品关联原料失败", e);
            return ResponseEntry.builder().code(-1).message("查询成品关联原料失败").build();
        }
    }

    /**
     * 保存生产记录
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(@RequestBody final ProductionPojo pojo) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            Pdhead existed = this.db().pdhead().getByFGSerialNo(loginUser.getCmpnid(), pojo.getFgSerialNo());
            if (ObjectUtil.isNotNull(existed)) {
                return ResponseEntry.builder().code(-1).message("生产序列号已存在").build();
            }

            for (PdlinePojo line : pojo.getProductionLines()) {
                Pdline pdline = this.db().pdline().getByRAWSerialNo(loginUser.getCmpnid(), line.getRawSerialNo());
                if (ObjectUtil.isNotNull(pdline)) {
                    return ResponseEntry.builder().code(-1).message("原料序列号[ " + pdline.getRwsrno() + " ]已被使用").build();
                }
            }

            Pdhead pdhead = new Pdhead();
            if (ObjectUtil.isNotNull(pojo.getSupplierId())) {
                pdhead.suplid(pojo.getSupplierId());
            }
            if (ObjectUtil.isNotNull(pojo.getProjectId())) {
                Projms projms = this.db().projms().get(pojo.getProjectId());
                pdhead.projid(projms.getProjid()).
                        cstmid(projms.getCstmid());
            }

            pdhead.cmpnid(loginUser.getCmpnid()).
                    rdocno(pojo.getRefDocNo()).
                    fgsrno(pojo.getFgSerialNo()).
                    pdmtid(pojo.getProductMaterialId()).
                    prddat(pojo.getProduceDate()).
                    isptor(pojo.getInspector()).
                    remark(pojo.getRemark()).
                    cretby(operatorBy).
                    crettm(currentTime).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().pdhead().save(pdhead);
            VPdhead vPdhead = new VPdhead().pdhdid(pdhead.getPdhdid()).oneById();

            // 记录日志
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(GR09.class.getSimpleName()).
                    name("生产记录管理").
                    action("生产录入").
                    message(StringUtil.format("生产序列号[{}]录入成功", pdhead.getFgsrno())).
                    newValue(vPdhead).
                    build();
            this.eventLogManager.info(event);

            for (PdlinePojo line : pojo.getProductionLines()) {
                Pdline pdline = new Pdline();
                pdline.cmpnid(loginUser.getCmpnid()).
                        pdhdid(pdhead.getPdhdid()).
                        rwsrno(line.getRawSerialNo()).
                        mtrlid(line.getMaterialId()).
                        cretby(operatorBy).
                        crettm(currentTime).
                        oprtby(operatorBy).
                        oprttm(currentTime);
                this.db().pdline().save(pdline);
                VPdline vPdline = new VPdline().pdlnid(pdline.getPdlnid()).oneById();

                // 记录日志
                event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR09.class.getSimpleName()).
                        name("生产记录管理").
                        action("生产录入").
                        message(StringUtil.format("生产序列号[{}] 原料序列号[{}]录入成功", pdhead.getFgsrno(), pdline.getRwsrno())).
                        newValue(vPdline).
                        build();
                this.eventLogManager.info(event);
            }
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("生产记录保存失败", e);
            throw new RestRuntimeException("生产记录保存失败");
        }
    }

    /**
     * 删除生产记录
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);

            for (Long id: ids) {
                VPdhead vPdhead = new VPdhead().pdhdid(id).oneById();
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(GR09.class.getSimpleName()).
                        name("生产记录管理").
                        action("删除").
                        message(StringUtil.format("生产序列号[{}]删除成功", vPdhead.getFgsrno())).
                        newValue(vPdhead).
                        build();
                this.eventLogManager.critical(event);

                // 删除生产记录行
                QueryWrapper wrapper = QueryWrapper.create();
                wrapper.where(PDLINE.PDHDID.eq(vPdhead.getPdhdid()));
                this.db().pdline().deleteQuery(wrapper);

                // 删除生产记录
                this.db().pdhead().delete(id);
            }

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("生产记录删除失败", e);
            throw new RestRuntimeException("生产记录已被使用");
        }
    }
}
