/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * 脚本动作按钮属性
 *
 * @author Berlin
 *******************************************************************************/
import AbstractAction from "@/ploutos/graces/ag01/faces/AbstractAction";

export default class ScriptLinkActionProps extends AbstractAction {

	/**
	 * 脚本函数
	 */
	link: string;
}