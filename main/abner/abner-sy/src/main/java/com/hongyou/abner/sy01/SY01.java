/*
 * Copyright 2014, Chengyou Software Development Studio.
 */
package com.hongyou.abner.sy01;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hongyou.abner.config.event.EventLog;
import com.hongyou.abner.config.web.UserDataProvider;
import com.hongyou.abner.data.model.Pmsnms;
import com.hongyou.abner.data.model.Rolems;
import com.hongyou.abner.data.model.Rolpms;
import com.hongyou.abner.data.model.Userms;
import com.hongyou.abner.data.pojo.RolemsPojo;
import com.hongyou.abner.sy01.pojo.FamilyOption;
import com.hongyou.abner.sy01.pojo.PermissionAction;
import com.hongyou.abner.sy01.pojo.PermissionMenu;
import com.hongyou.baron.exceptions.RestRuntimeException;
import com.hongyou.baron.logging.Log;
import com.hongyou.baron.logging.LogFactory;
import com.hongyou.baron.util.JsonUtil;
import com.hongyou.baron.util.ListUtil;
import com.hongyou.baron.util.ObjectUtil;
import com.hongyou.baron.util.StringUtil;
import com.hongyou.baron.web.ResponseEntry;
import com.hongyou.baron.web.navigation.Family;
import com.hongyou.baron.web.navigation.Navigate;
import com.hongyou.baron.web.navigation.NavigationManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色管理
 *
 * @author Berlin
 */
@RestController
@RequestMapping("/sy01")
public class SY01 extends UserDataProvider {

    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(SY01.class);

    /**
     * 导航菜单管理器
     */
    private final NavigationManager navigationManager;

    /**
     * 注入服务
     */
    public SY01(final NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
    }

    /**
     * 保存角色
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry save(
            @RequestBody final RolemsPojo rolemsPojo, final HttpServletRequest request
    ) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();

            // 修改
            Rolems rolems = null; Rolems oldRolems = null;
            if (ObjectUtil.isNotNull(rolemsPojo.getId())) {
                rolems = this.db().rolems().get(rolemsPojo.getId());
                oldRolems = (Rolems) rolems.clone();
            }

            // 新增
            if (ObjectUtil.isNull(rolems)) {
                rolems = new Rolems();
                rolems.cmpnid(loginUser.getCmpnid()).
                        cretby(operatorBy).
                        crettm(currentTime);
            }

            // 检查是否已存在
            if (!ObjectUtil.equal(rolemsPojo.getName(), rolems.getRolenm())) {
                Rolems existed = this.db().rolems().getByName(loginUser.getCmpnid(), rolemsPojo.getName());
                if (ObjectUtil.isNotNull(existed)) {
                    return ResponseEntry.builder().code(-1).message("角色名称已存在").build();
                }
            }

            rolems.rolenm(rolemsPojo.getName()).
                    remark(rolemsPojo.getRemark()).
                    oprtby(operatorBy).
                    oprttm(currentTime);
            this.db().rolems().save(rolems);

            // 记录日志
            String action = oldRolems == null ? "新增" : "修改";
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "rolems");
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(SY01.class.getSimpleName()).
                    name("角色管理").
                    action(action).
                    message(StringUtil.format("角色[{}]{}成功", rolems.getRolenm(), action)).
                    oldValue(oldRolems).
                    newValue(rolems).
                    enumsDisplay(displays).
                    build();
            this.eventLogManager.info(event);

            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("角色保存失败", e);
            throw new RestRuntimeException("角色保存失败");
        }
    }

    /**
     * 删除角色
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry delete(@RequestBody final List<Long> ids, final HttpServletRequest request) {

        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Map<String, String> displays = this.international.getTableValuesDisplay(request, "rolems");

            for (Long id: ids) {
                Rolems rolems = this.db().rolems().get(id);
                EventLog event = EventLog.builder().
                        domain(loginUser.getCmpnid()).
                        operator(operatorBy).
                        module(SY01.class.getSimpleName()).
                        name("角色管理").
                        action("删除").
                        message(StringUtil.format("角色[{}]删除成功", rolems.getRolenm())).
                        newValue(rolems).
                        enumsDisplay(displays).
                        build();
                this.eventLogManager.critical(event);
                this.db().rolems().delete(rolems);
            }
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("角色删除失败", e);
            throw new RestRuntimeException("角色已被使用");
        }
    }

    /**
     * 权限分配
     *
     * @param roleId 角色ID
     * @param permissionMenus 菜单权限
     */
    @PostMapping("/assign/{roleId}")
    @Transactional(rollbackFor = RestRuntimeException.class)
    public ResponseEntry assign(
            @PathVariable final Long roleId, @RequestBody final List<PermissionMenu> permissionMenus
    ) {
        try {
            Userms loginUser = this.getLoginUser();
            String operatorBy = this.getOperatorBy(loginUser);
            Timestamp currentTime = this.getCurrentTime();
            Rolems rolems = this.db().rolems().get(roleId);

            // 角色已经分配的权限ID
            List<Rolpms> rolpmss = this.db().rolpms().listByRole(rolems.getRoleid());
            List<Long> assignedIds = rolpmss.stream().map(Rolpms::getPmsnid).toList();
            Map<Long, Long> rlpmids = rolpmss.stream().collect(Collectors.toMap(Rolpms::getPmsnid, Rolpms::getRlpmid));

            // 记录分配与取消分配的权限
            List<Long> deleteIds = new ArrayList<>();
            List<Rolpms> assignRolpmss = new ArrayList<>();

            permissionMenus.forEach(menu -> menu.getPermissionActions().forEach(action -> {
                // 分配权限
                if (action.isAssigned() && !assignedIds.contains(action.getPermissionId())) {
                    assignRolpmss.add(new Rolpms().
                            roleid(roleId).
                            pmsnid(action.getPermissionId()).
                            oprtby(operatorBy).
                            oprttm(currentTime)
                    );
                }
                // 取消权限
                if (!action.isAssigned() && assignedIds.contains(action.getPermissionId())) {
                    deleteIds.add(rlpmids.get(action.getPermissionId()));
                }
            }));

            if (ListUtil.isNotEmpty(assignRolpmss)) {
                this.db().rolpms().insertBatch(assignRolpmss);
            }
            if (ListUtil.isNotEmpty(deleteIds)) {
                this.db().rolpms().deleteIds(deleteIds);
            }

            // 记录日志
            EventLog event = EventLog.builder().
                    domain(loginUser.getCmpnid()).
                    operator(operatorBy).
                    module(SY01.class.getSimpleName()).
                    name("角色管理").
                    action("权限分配").
                    message(StringUtil.format("角色[{}]权限分配成功", rolems.getRolenm())).
                    build();
            this.eventLogManager.info(event);
            return ResponseEntry.SUCCESS;
        } catch (Exception e) {
            logger.error("权限分配失败", e);
            throw new RestRuntimeException("权限分配失败");
        }
    }

    /**
     * 加载导航族
     */
    @GetMapping("/loadFamilies")
    public ResponseEntry getFamilies() {

        try {
            // 获取到所有导航族选项
            List<Family> families = this.navigationManager.getFamilies();
            List<FamilyOption> familyOptions = new ArrayList<>();
            families.forEach(i -> familyOptions.add(
                    new FamilyOption(i.getFamilyLabel(), i.getFamilyName())
            ));

            return ResponseEntry.builder().body(familyOptions).build();
        } catch (Exception e) {
            logger.error("查询导航族失败", e);
            return ResponseEntry.builder().code(-1).message("查询导航族失败").build();
        }
    }

    /**
     * 加载航族菜单权限
     */
    @GetMapping("/loadPermissions/{family}/{local}/{roleId}")
    public ResponseEntry getPermissions(
            @PathVariable final String family, @PathVariable final String local, @PathVariable final Long roleId
    ) {

        try {
            // 获取导航族定义的菜单
            List<Navigate> menus = this.navigationManager.load(family);
            if (menus == null) {
                return ResponseEntry.builder().body(new ArrayList<>()).build();
            }
            Userms loginUser = this.getLoginUser();

            // 加载系统定义的权限
            List<Pmsnms> pmsnmss = this.db().pmsnms().listByCompany(loginUser.getCmpnid(), local);
            Map<String, List<Pmsnms>> permissions = pmsnmss.stream().collect(
                    Collectors.groupingBy(Pmsnms::getPmsncd));

            // 给角色分配的权限
            Rolems rolems = this.db().rolems().get(roleId);
            List<Rolpms> rolpmss = this.db().rolpms().listByRole(roleId);
            List<Long> assignedIds = rolpmss.stream().map(Rolpms::getPmsnid).toList();

            // 递归加载菜单权限
            List<PermissionMenu> permissionMenus = this.getPermissionMenus(menus, permissions, assignedIds);

            ObjectNode result = JsonUtil.createObjectNode();
            result.put("roleName", rolems.getRolenm());
            result.put("superAdmin", Rolems.SUPADM.Yes.equals(rolems.getSupadm()));
            result.set("permissionMenus", JsonUtil.convertValue(permissionMenus));
            return ResponseEntry.builder().body(result).build();
        } catch (Exception e) {
            logger.error("查询导航族菜单权限失败", e);
            return ResponseEntry.builder().code(-1).message("查询导航族菜单权限失败").build();
        }
    }

    /**
     * 递归加载菜单权限
     *
     * @param menus 菜单集合
     * @param permissions 系统定义的权限
     * @param assignedIds 当前角色已分配的权限
     */
    private List<PermissionMenu> getPermissionMenus(
            final List<Navigate> menus, final Map<String, List<Pmsnms>> permissions,
            final List<Long> assignedIds
    ) {
        List<PermissionMenu> permissionMenus = new ArrayList<>();
        for (Navigate menu : menus) {
            PermissionMenu.PermissionMenuBuilder permissionMenu = PermissionMenu.builder();

            // 有子菜单
            if (ListUtil.isNotEmpty(menu.getChildren())) {
                permissionMenu.children(this.getPermissionMenus(menu.getChildren(), permissions, assignedIds));
            }
            List<PermissionAction> permissionActions = new ArrayList<>();

            // 当前菜单权限
            List<Pmsnms> pmsnmss = permissions.get(menu.getId());
            if (ListUtil.isNotEmpty(pmsnmss)) {
                List<PermissionMenu> childrenPermissions = new ArrayList<>();
                pmsnmss.forEach(pmsnms -> {
                    // 当前菜单的权限
                    permissionActions.add(PermissionAction.builder().
                            permissionId(pmsnms.getPmsnid()).
                            permissionCode(pmsnms.getPmsncd()).
                            permissionName(menu.getLabel()).
                            actionCode(pmsnms.getActcde()).
                            actionName(pmsnms.getActnam()).
                            assigned(assignedIds.contains(pmsnms.getPmsnid())).
                            build()
                    );

                    // 将权限也作为子菜单
                    childrenPermissions.add(PermissionMenu.builder().
                            treeId(menu.getId() + "@" + pmsnms.getActcde()).
                            label(pmsnms.getActnam()).
                            isPermission(true).build());
                });
                permissionMenu.isMenu(true).
                        children(childrenPermissions);
            }
            permissionMenus.add(permissionMenu.
                    treeId(menu.getId()).
                    label(menu.getLabel()).
                    permissionActions(permissionActions).
                    build());
        }
        return permissionMenus;
    }
}
