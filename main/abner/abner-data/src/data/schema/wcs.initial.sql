/**
 * 公司
 */
insert into cmpnms (cmpnid, cmpncd, cmpnnm, zipcde, addres, contcs, phonno, email, remark, oprtby) values (1, '001', '门店', '', '', '', '', '', ' ', 'anonymous');

/**
 * 序列号生成策略
 */

/**
 * 定时任务器
 */
insert into schjob (cmpnid, scgrup, scname, jbgrup, jbname, enabld, dlyscd, schtyp, schcfg, schtim, param1, param2, param3, param4, param5, param6, param7, param8, param9, remark) VALUES (1, 'WCS', 'AM00/Heartbeat', 'WCS', 'AM00/Heartbeat', 'N', '1', 'C', '0 */1 * * * ?', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '每1分钟检测设备心跳');
insert into schjob (cmpnid, scgrup, scname, jbgrup, jbname, enabld, dlyscd, schtyp, schcfg, schtim, param1, param2, param3, param4, param5, param6, param7, param8, param9, remark) VALUES (1, 'WCS', 'AM00/ClearIgnore', 'WCS', 'AM00/ClearIgnore', 'N', '1', 'C', '0 */1 * * * ?', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '每1分钟清除动态忽略标签');

/**
 * 操作员 密码: aaa
 */
insert into oprtms (oprtid, cmpnid, accunt, paswrd, gender, wcvrfy, emvrfy, status, fstlgn, lgerct, oprtby) values (1, 1, 'admin', '7e24dffffffe74fffffffb1ffffffed8fffffffa8ffffffd3ffffff8063fffffff6ffffffa6ffffffa91462ffffffa815', 'M', 'N', 'N', '90', 'Y', 0, 'anonymous');

/**
  * 权限
  */
insert into pmsnms (pmsnid, cmpnid, pmsnam, remark, actnam) values (1, 1, 'ba08', '角色维护', '浏览');
insert into pmsnms (pmsnid, cmpnid, pmsnam, remark, actnam) values (2, 1, 'ba08', '角色维护', '权限分配');

insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba08', '角色维护', '新增');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba08', '角色维护', '修改');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba08', '角色维护', '删除');

insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba01', '公司管理', '浏览');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba01', '公司管理', '编辑');

insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba02', '区域维护', '浏览');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba02', '区域维护', '新增');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba02', '区域维护', '修改');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba02', '区域维护', '删除');

insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba03', '门店维护', '浏览');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba03', '门店维护', '新增');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba03', '门店维护', '修改');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba03', '门店维护', '删除');

insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba04', '门店联系方式', '浏览');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba04', '门店联系方式', '新增');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba04', '门店联系方式', '修改');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba04', '门店联系方式', '删除');

insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba05', '门店忽略标签', '浏览');

insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba06', '供应商维护', '浏览');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba06', '供应商维护', '新增');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba06', '供应商维护', '修改');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba06', '供应商维护', '删除');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba06', '供应商维护', '有效供应商');

insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba09', '操作员维护', '浏览');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba09', '操作员维护', '新增');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba09', '操作员维护', '修改');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba09', '操作员维护', '删除');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba09', '操作员维护', '重置密码');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba09', '操作员维护', '角色分配');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba09', '操作员维护', '门店分配');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba09', '操作员维护', '冻结人员');

insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba11', '产品维护', '浏览');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba11', '产品维护', '新增');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba11', '产品维护', '修改');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba11', '产品维护', '删除');

insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba12', '物料维护', '浏览');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba12', '物料维护', '新增');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba12', '物料维护', '修改');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'ba12', '物料维护', '删除');

insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am01', '设备类型维护', '浏览');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am01', '设备类型维护', '新增');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am01', '设备类型维护', '修改');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am01', '设备类型维护', '删除');

insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am02', '设备维护', '浏览');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am02', '设备维护', '新增');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am02', '设备维护', '修改');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am02', '设备维护', '删除');

insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am03', '设备天线维护', '浏览');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am03', '设备天线维护', '新增');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am03', '设备天线维护', '修改');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am03', '设备天线维护', '删除');

insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am05', '设备参数维护', '浏览');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am05', '设备参数维护', '修改');

insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am04', '门禁数据', '浏览');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am06', '试衣数据', '浏览');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am07', '设备视图', '浏览');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am08', '设备日志', '浏览');
insert into pmsnms (cmpnid, pmsnam, remark, actnam) values (1, 'am09', '收银数据', '浏览');

/**
 * 角色表
 */
insert into rolems (roleid, cmpnid, rolenm, CRETBY) VALUES (1, 1, '超级管理员', 'admin');

/**
 * 角色操作员关系表
 */
insert into oprrol (OPRLID, ROLEID, OPRTID) VALUES (1, 1, 1);

/**
 * 角色权限关系
 */
insert into rolpms(rlpmid, roleid, pmsnid) values (1, 1 ,1);
insert into rolpms(rlpmid, roleid, pmsnid) values (2, 1 ,2);

/**
 * 初始化设备类型
 */
insert into eqptyp (cmpnid, eqtycd, eqtynm) value (1, 'Sales', '收银设备');
insert into eqptyp (cmpnid, eqtycd, eqtynm) value (1, 'Door', '门禁设备');
insert into eqptyp (cmpnid, eqtycd, eqtynm) value (1, 'Fitting', '试衣设备');
insert into eqptyp (cmpnid, eqtycd, eqtynm) value (1, 'List', '陈列设备');
insert into eqptyp (cmpnid, eqtycd, eqtynm) value (1, 'Show', '展示设备');


/**
 * 门禁设备缺省参数
 */
insert into dfeqpm(cmpnid, sortng, eqtycd, parmcd, parmnm, parmty, parmvl, remark) values (1, 1, 'Door', 'epcRules', '门禁EPC掩码规则', 'String', '', '门禁掩码规则，符合此规则则报警，多个以英文“;”分隔');
insert into dfeqpm(cmpnid, sortng, eqtycd, parmcd, parmnm, parmty, parmvl, remark) values (1, 2, 'Door', 'buzzer', '是否打开蜂鸣器', 'Bool', 'true', '扫描到非法标签时是否打开蜂鸣器');
insert into dfeqpm(cmpnid, sortng, eqtycd, parmcd, parmnm, parmty, parmvl, remark) values (1, 3, 'Door', 'buzzerGpio', '蜂鸣器Gpio', 'Int', '1', '扫描到非法标签时打开蜂鸣器的Gpio口号');
insert into dfeqpm(cmpnid, sortng, eqtycd, parmcd, parmnm, parmty, parmvl, remark) values (1, 4, 'Door', 'alarmLamp', '是否打开报警灯', 'Bool', 'true', '扫描到非法标签时是否打开报警灯');
insert into dfeqpm(cmpnid, sortng, eqtycd, parmcd, parmnm, parmty, parmvl, remark) values (1, 5, 'Door', 'alarmLampGpio', '报警灯Gpio', 'Int', '1', '扫描到非法标签时打开报警灯的Gpio口号');
insert into dfeqpm(cmpnid, sortng, eqtycd, parmcd, parmnm, parmty, parmvl, remark) values (1, 6, 'Door', 'passLamp', '是否打开通行灯', 'Bool', 'true', '扫描到正确标签时打开通行灯');
insert into dfeqpm(cmpnid, sortng, eqtycd, parmcd, parmnm, parmty, parmvl, remark) values (1, 7, 'Door', 'passLampGpio', '通行灯Gpio', 'Int', '1', '扫描到正确标签时打开通行灯的Gpio口号');
insert into dfeqpm(cmpnid, sortng, eqtycd, parmcd, parmnm, parmty, parmvl, remark) values (1, 8, 'Door', 'gpioDelayTime', 'Gpio延迟关闭时间(秒)', 'Int', '5', 'Gpio延迟关闭时间(秒)');
insert into dfeqpm(cmpnid, sortng, eqtycd, parmcd, parmnm, parmty, parmvl, remark) values (1, 9, 'Door', 'connectDelayTime', '延时连接读写器(秒)', 'Int', '60', '程序启动后延时连接读写器时间(秒)');
insert into dfeqpm(cmpnid, sortng, eqtycd, parmcd, parmnm, parmty, parmvl, remark) values (1, 10, 'Door', 'ignoreMethod', '误读忽略机制', 'String', '60:5', '在多少秒内读取到多少次将忽略标签(60:5)');
insert into dfeqpm(cmpnid, sortng, eqtycd, parmcd, parmnm, parmty, parmvl, remark) values (1, 11, 'Door', 'saveIgnore', '是否保存忽略标签记录', 'Bool', 'false', '是否保存忽略标签记录');

/**
 * 试衣设备缺省参数
 */
insert into dfeqpm(cmpnid, sortng, eqtycd, parmcd, parmnm, parmty, parmvl, remark) values (1, 9, 'Fitting', 'connectDelayTime', '延时连接读写器(秒)', 'Int', '60', '程序启动后延时连接读写器时间(秒)');
insert into dfeqpm(cmpnid, sortng, eqtycd, parmcd, parmnm, parmty, parmvl, remark) values (1, 1, 'Fitting', 'epcExpireGapTime', 'epc读取有效间隔时间(秒)', 'Int', '12', '在多少秒内epc读取不到算有效数据');

/**
 * 初始化SQL（安装数据库时不需要执行）
 */
insert into stigtg (streid, rfidcd, igntyp) values (null, '', 'F');
insert into eqparm (eqpmid, sortng, parmcd, parmnm, parmty, parmvl, remark) values (null, 0, '', '', '', '', '');