/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import type {NotificationApiInjection} from "naive-ui/es/notification/src/NotificationProvider";
import {useNotification} from "naive-ui";
/********************************************************************************
 * 提示信息反馈组件
 *
 * @author Berlin
 *******************************************************************************/

export namespace Message {

  let notification: NotificationApiInjection;

  /**
   * 警告
   */
  export function warning(content: string) {
    notification.warning({ title: content, duration: 3000 });
  }

  /**
   * 异常
   */
  export function error(content: string) {
    notification.error({ title: content, duration: 3000 });
  }

  /**
   * 初始化通知消息
   */
  export function init() {
    notification = useNotification();
  }
}
