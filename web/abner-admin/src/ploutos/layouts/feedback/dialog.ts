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
  title: string | (() => import("vue").VNodeChild);

  /**
   * 内容
   */
  content: string | (() => import("vue").VNodeChild);

  /**
   * 确认文件
   */
  confirmText: string,

  /**
   * 取消文件
   */
  cancelText?: string,

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
   * 警告弹框
   */
  export function warning(options: DialogOption) {
    dialog.warning({
      title: options.title,
      content: options.content,
      positiveText: options.confirmText,
      negativeText: options.cancelText,
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
