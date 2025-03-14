/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {useMessage} from "naive-ui";
import type {MessageApiInjection} from "naive-ui/es/message/src/MessageProvider";
import type {MessageOptions} from "naive-ui/es/message/src/types";
/********************************************************************************
 * 提示信息组件
 *
 * @author Berlin
 *******************************************************************************/
namespace Message {

  /**
   * 引入提示信息组件
   */
  let message: MessageApiInjection;

  /**
   * 加载信息
   */
  export function loading(content: string, options?: MessageOptions) {
    return message.loading(content, options);
  }

  /**
   * 成功信息
   */
  export function success(content: string, options?: MessageOptions) {
    return message.success(content, options);
  }

  /**
   * 错误信息
   */
  export function error(content: string, options?: MessageOptions) {
    return message.error(content, options);
  }

  /**

  /**
   * 初始化提示信息组件
   */
  export function init() {
    message = useMessage();
  }
}
export default Message;
