/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {useDialog} from "naive-ui";
import type {DialogApiInjection, DialogOptions} from "naive-ui/es/dialog/src/DialogProvider";
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
  content?: string | (() => import("vue").VNodeChild) | any;

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
  onConfirmClick?: () => Promise<unknown> | unknown;

  /**
   * 点击取消
   */
  onNegativeClick?: () => Promise<unknown> | unknown;

  /**
   * 是否有关闭图标
   */
  closable?: boolean
};

namespace Dialog {

  /**
   * 引入对话框组件
   */
  let dialog: DialogApiInjection;

  /**
   * 上次点击时间戳(实现防抖)
   */
  let lastClickTime = new Date().getTime();

  /**
   * 信息弹框
   */
  export function info(options: DialogOption) {
    dialog.info({
      ...commandOptions(options),
      content: options.content
    });
  }

  /**
   * 成功弹框
   */
  export function success(options: DialogOption) {
    dialog.success({
      ...commandOptions(options),
      content: options.content ? options.content : '操作成功'
    });
  }

  /**
   * 警告弹框
   */
  export function warning(options: DialogOption) {
    dialog.warning({
      ...commandOptions(options),
      content: () => options.content ? options.content : '是否确认？',
    });
  }

  /**
   * 异常弹框
   */
  export function error(options: DialogOption) {
    dialog.error({
      ...commandOptions(options),
      content: options.content ? options.content : '操作异常'
    });
  }

  /**
   * 弹框提示共同的属性
   */
  function commandOptions(options: DialogOption): DialogOptions {
    return {
      draggable: true,
      closable: options.closable,
      closeOnEsc: options.closable,
      maskClosable: options.closable,
      title: options.title ? options.title : '提示',
      positiveText: options.confirmText ? options.confirmText : '确认',
      negativeText: options.cancelText ? options.cancelText : options.noCancel ? undefined : '取消',
      onPositiveClick: () => {
        if (!options.onConfirmClick) {
          return;
        }
        // 防抖
        const currentTime = new Date().getTime();
        if ((currentTime - lastClickTime) / 1000 > 1) {
          lastClickTime = currentTime;
          options.onConfirmClick();
        }
      },
    }
  }

  /**
   * 初始化对话框组件
   */
  export function init() {
    dialog = useDialog();
  }
}
export default Dialog;
