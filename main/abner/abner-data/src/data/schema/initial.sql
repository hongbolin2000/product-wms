/**
 * 公司
 */
insert into cmpnms(cmpnid, cmpncd, cmpnnm, pftitl, pfstil, pfsptl) values (1, '100', '初始化公司', '初始化平台', 'Initial Platform', '初始平台');

/**
 * 序列号
 */
insert into srgnhd (srgtyp, srgstg, srptrn, srprfx, srllen) values ('suplms.suplcd', 'DIGITAL', '', 'G', 6);
insert into srgnhd (srgtyp, srgstg, srptrn, srprfx, srllen) values ('projms.projcd', 'DIGITAL', '', 'J', 6);
insert into srgnhd (srgtyp, srgstg, srptrn, srprfx, srllen) values ('rqhead.rqhdno', 'DATE', 'yyMM', 'RQ', 10);
insert into srgnhd (srgtyp, srgstg, srptrn, srprfx, srllen) values ('pohead.pohdno', 'DATE', 'yyMM', 'PO', 10);
insert into srgnhd (srgtyp, srgstg, srptrn, srprfx, srllen) values ('rohead.rvodno', 'DATE', 'yyMM', 'RO', 10);
insert into srgnhd (srgtyp, srgstg, srptrn, srprfx, srllen) values ('stckim.bathno', 'DATE', 'yyMM', 'BT', 10);
insert into srgnhd (srgtyp, srgstg, srptrn, srprfx, srllen) values ('stckim.stimno', 'DATE', 'yyMM', '9', 10);
insert into srgnhd (srgtyp, srgstg, srptrn, srprfx, srllen) values ('sohead.sohdno', 'DATE', 'yyMM', 'SO', 10);
insert into srgnhd (srgtyp, srgstg, srptrn, srprfx, srllen) values ('dohead.dlodno', 'DATE', 'yyMM', 'DO', 10);

/**
 * 用户(密码: 12345678)
 */
insert into userms(userid, cmpnid, usernm, paswrd, fulnam) values (1, 1, 'admin', '2fb5b2be5fade0ffaa98d554c81b7e85', '超级管理员');

/**
  * 权限
  */
insert into pmsnms(pmsnid, cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 1, 'sy01', '角色管理', 'browse', '查看');
insert into pmsnms(pmsnid, cmpnid, sortng, pmsncd, remark, actcde, actnam) values (2, 1, 5, 'sy01', '角色管理', 'permission.assign', '权限分配');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'sy01', '角色管理', 'add', '新增');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'sy01', '角色管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'sy01', '角色管理', 'delete', '删除');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'sy02', '用户管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'sy02', '用户管理', 'add', '新增');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'sy02', '用户管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'sy02', '用户管理', 'delete', '删除');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 5, 'sy02', '用户管理', 'role.assign', '角色分配');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 6, 'sy02', '用户管理', 'warehouse.assign', '仓库分配');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 7, 'sy02', '用户管理', 'frozen', '冻结');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 8, 'sy02', '用户管理', 'unfrozen', '解除冻结');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 9, 'sy02', '用户管理', 'reset.pwd', '重置密码');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'sy03', '公司管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'sy03', '公司管理', 'update', '修改');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'sy04', '系统日志', 'browse', '查看');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'sy05', '系统任务', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'sy05', '系统任务', 'run', '运行');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'sy05', '系统任务', 'enable', '启用');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'sy05', '系统任务', 'disable', '禁用');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'ba01', '供应商管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'ba01', '供应商管理', 'add', '新增');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'ba01', '供应商管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'ba01', '供应商管理', 'delete', '删除');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'ba02', '项目管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'ba02', '项目管理', 'add', '新增');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'ba02', '项目管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'ba02', '项目管理', 'delete', '删除');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'ba03', '项目文档管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'ba03', '项目文档管理', 'add', '新增');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'ba03', '项目文档管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'ba03', '项目文档管理', 'delete', '删除');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'ba04', '项目进度管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'ba04', '项目进度管理', 'add', '新增');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'ba04', '项目进度管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'ba04', '项目进度管理', 'delete', '删除');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'ba05', '项目工作日志', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'ba05Report', '工作日志填报', 'add', '填报');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'wb01', '物料类型管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'wb01', '物料类型管理', 'add', '新增');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'wb01', '物料类型管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'wb01', '物料类型管理', 'delete', '删除');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'wb02', '物料管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'wb02', '物料管理', 'add', '新增');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'wb02', '物料管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'wb02', '物料管理', 'delete', '删除');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'wb02', '物料管理', 'raw.assign', '原料关联');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'wb03', '仓库管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'wb03', '仓库管理', 'add', '新增');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'wb03', '仓库管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'wb03', '仓库管理', 'delete', '删除');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'wb04', '库区管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'wb04', '库区管理', 'add', '新增');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'wb04', '库区管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'wb04', '库区管理', 'delete', '删除');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'wb05', '库位管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'wb05', '库位管理', 'add', '新增');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'wb05', '库位管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'wb05', '库位管理', 'delete', '删除');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'gr01', '请购单管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'gr01', '请购单管理', 'add', '创建请购单');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'gr01', '请购单管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'gr01', '请购单管理', 'delete', '删除');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 5, 'gr01', '请购单管理', 'audit', '审核通过');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 6, 'gr01', '请购单管理', 'issue.po', '下发采购');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'gr03', '采购单管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'gr03', '采购单管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'gr03', '采购单管理', 'delete', '删除');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'gr03', '采购单管理', 'audit', '审核通过');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 5, 'gr03', '采购单管理', 'issue.ro', '下发收货');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 6, 'gr03', '采购单管理', 'payment.entry', '付款录入');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'gr06', '付款记录管理', 'browse', '查看');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'gr07', '收货单管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'gr07', '收货单管理', 'add', '创建收货单');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'gr07', '收货单管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'gr07', '收货单管理', 'delete', '删除');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 5, 'gr07', '收货单管理', 'audit', '审核通过');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 6, 'gr07', '收货单管理', 'batch.receive', '批次收货');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 7, 'gr07', '收货单管理', 'serial.receive', '序列号收货');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'dn01', '销售单管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'dn01', '销售单管理', 'add', '创建销售单');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'dn01', '销售单管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'dn01', '销售单管理', 'delete', '删除');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 5, 'dn01', '销售单管理', 'audit', '审核通过');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 6, 'dn01', '销售单管理', 'issue.do', '下发发货');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 5, 'dn01', '销售单管理', 'receipt.entry', '收款录入');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'dn03', '发货单管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'dn03', '发货单管理', 'add', '创建发货单');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'dn03', '发货单管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'dn03', '发货单管理', 'delete', '删除');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 5, 'dn03', '发货单管理', 'audit', '审核通过');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 6, 'dn03', '发货单管理', 'batch.deliver', '批次发货');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 7, 'dn03', '发货单管理', 'serial.deliver', '序列号发货');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'dn06', '收款记录管理', 'browse', '查看');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'wm01', '盘点任务管理', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 2, 'wm01', '盘点任务管理', 'add', '创建盘点单');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 3, 'wm01', '盘点任务管理', 'update', '修改');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 4, 'wm01', '盘点任务管理', 'delete', '删除');

insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'wr01', '库存报表', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'wr02', '库存明细报表', 'browse', '查看');
insert into pmsnms(cmpnid, sortng, pmsncd, remark, actcde, actnam) values (1, 1, 'wr03', '库存日志报表', 'browse', '查看');

/**
 * 角色
 */
insert into rolems (roleid, cmpnid, rolenm, supadm) values (1, 1, '超级管理员', 'Y');

/**
 * 角色用户关系
 */
insert into usrrol (roleid, userid) values (1, 1);

/**
 * 角色权限关系
 */
insert into rolpms(roleid, pmsnid) values (1 ,1);
insert into rolpms(roleid, pmsnid) values (1 ,2);