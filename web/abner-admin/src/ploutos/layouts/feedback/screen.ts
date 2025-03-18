/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import screenfull from 'screenfull'
/********************************************************************************
 * 全屏组件
 *
 * @author Berlin
 *******************************************************************************/
import {message} from "@/ploutos";
import useLayoutStore from "@/ploutos/layouts/store/layout-store.ts";

namespace Screen {

  /**
   * 全屏
   */
  export async function full() {
    if (!enabled()) {
      return;
    }
    await screenfull.toggle();
  }

  /**
   * 指定元素全屏
   *
   * @param elementId 元素ID
   * @param appendClassNames 追加的样式类
   */
  export async function fullElement(elementId: string, appendClassNames: string[]) {
    if (!enabled()) {
      return;
    }
    const element: HTMLElement | any = document.getElementById(elementId);
    element.classList.add(appendClassNames);
    await screenfull.toggle(element);

    // 退出全屏
    screenfull.onchange(() => {
      if (!screenfull.isFullscreen) {
        const layoutStore = useLayoutStore();
        layoutStore.isFullScreen = false;
        element.classList.remove(appendClassNames);
      }
    });
  }

  /**
   * 浏览器是否支持全屏
   */
  function enabled(): boolean {
    if (!screenfull.isEnabled) {
      message.error('当前浏览器不支持全屏操作')
      return false
    }
    return true;
  }
}
export default Screen;
