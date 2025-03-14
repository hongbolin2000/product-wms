/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {useDialog} from "naive-ui";
import type {DialogApiInjection} from "naive-ui/es/dialog/src/DialogProvider";
/********************************************************************************
 * 对话框组件
 *
 * @author Berlin
 *******************************************************************************/
type DialogOption = {

  /**
   * 标题
   */
  title?: string | (() => import("vue").VNodeChild);

  /**
   * 内容
   */
  content?: string | (() => import("vue").VNodeChild);

  /**
   * 确认文字
   */
  confirmText?: string,

  /**
   * 取消文字
   */
  cancelText?: string,

  /**
   * 是否显示取消按钮
   */
  noCancel?: boolean,

  /**
   * 点击确认
   */
  onConfirmClick: () => Promise<unknown> | unknown;

  /**
   * 点击取消
   */
  onNegativeClick?: () => Promise<unknown> | unknown;
};

namespace Dialog {

  /**
   * 引入对话框组件
   */
  let dialog: DialogApiInjection;

  /**
   * 成功弹框
   */
  export function success(options: DialogOption) {
    dialog.success({
      title: options.title ? options.title : '提示',
      content: options.content ? options.content : '是否确认？',
      positiveText: options.confirmText ? options.confirmText : '确认',
      negativeText: options.cancelText ? options.cancelText : options.noCancel ? undefined : '取消',
      onPositiveClick: () => options.onConfirmClick(),
    });
  }

  /**
   * 警告弹框
   */
  export function warning(options: DialogOption) {
    dialog.warning({
      title: options.title ? options.title : '提示',
      content: options.content ? options.content : '是否确认？',
      positiveText: options.confirmText ? options.confirmText : '确认',
      negativeText: options.cancelText ? options.cancelText : options.noCancel ? undefined : '取消',
      onPositiveClick: () => options.onConfirmClick(),
    });
  }

  /**
   * 初始化对话框组件
   */
  export function init() {
    dialog = useDialog();
  }
}
export default Dialog;
