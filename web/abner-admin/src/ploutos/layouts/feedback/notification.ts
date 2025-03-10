/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import type {NotificationApiInjection} from "naive-ui/es/notification/src/NotificationProvider";
import {useNotification} from "naive-ui";
/********************************************************************************
 * 通知消息组件
 *
 * @author Berlin
 *******************************************************************************/
namespace Notification {

  /**
   * 引入通知消息组件
   */
  let notification: NotificationApiInjection;

  /**
   * 警告消息
   */
  export function warning(content: string) {
    notification.warning({ title: content, duration: 3000 });
  }

  /**
   * 异常消息
   */
  export function error(content: string) {
    notification.error({ title: content, duration: 3000 });
  }

  /**
   * 初始化通知消息组件
   */
  export function init() {
    notification = useNotification();
  }
}
export default Notification;
