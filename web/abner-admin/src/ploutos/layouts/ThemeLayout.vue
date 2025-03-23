<template>
  <n-config-provider :theme-overrides="themeOverrides" :theme="theme" :locale="zhCN" style="height: 100%">
    <n-global-style/>
      <n-scrollbar>
        <n-spin :show="layoutStore.loading">
          <template #icon>
            <n-icon>
              <SettingsOutline />
            </n-icon>
          </template>

          <n-el class="vaw-layout-container"
                :class="[layoutStore.deviceType === 'mobile' && 'is-mobile']"
                :style="{backgroundColor: layoutStore.themeBgColor}"
          >
            <template v-if="layoutStore.layoutMode === LayoutMode.TopBottom || layoutStore.layoutMode === LayoutMode.TopLeft">
              <SideBar v-if="layoutStore.layoutMode === LayoutMode.TopLeft"/>
              <VawHeader/>
            </template>
            <template v-else-if="layoutStore.layoutMode === LayoutMode.LeftSplit">
              <SplitSideBar/>
            </template>
            <template v-else>
              <SideBar/>
            </template>

            <MainLayout/>

            <div class="mobile-shadow" @click="closeMenu"
                 v-if="layoutStore.deviceType === 'mobile'"
                 :class="[layoutStore.isCollapse ? 'close-shadow' : 'show-shadow']"
            />
          </n-el>
          <n-back-top/>
        </n-spin>
      </n-scrollbar>
  </n-config-provider>
</template>

<script setup lang="tsx">
  import {computed, type ComputedRef, onBeforeUnmount, onMounted} from "vue"
  import {darkTheme, zhCN} from 'naive-ui'
  import {SettingsOutline} from '@vicons/ionicons5';
  /********************************************************************************
   * 框架布局
   *
   * @author Berlin
   ********************************************************************************/
  import '@/ploutos/layouts/styles'
  import {DeviceType, LayoutMode, ThemeMode} from '@/ploutos/layouts/types'
  import SideBar from '@/ploutos/layouts/sidebar/SideBar.vue'
  import MainLayout from "@/ploutos/layouts/MainLayout.vue";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";
  import VawHeader from "@/ploutos/layouts/header/VawHeader.vue";
  import SplitSideBar from "@/ploutos/layouts/sidebar/SplitSideBar.vue";

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

  /**
   * 主题样式
   */
  const themeOverrides = computed(() => {
    const common = {
      primaryColor: layoutStore.themeColor,
      primaryColorHover: layoutStore.themeColor,
    }

    if (layoutStore.theme == ThemeMode.DARK) {
      common['borderColor'] = 'white'
    }
    return {
      common: common,
      Button: {
        // primary类型按钮
        colorPrimary: layoutStore.themeColor, // 背景颜色
        colorHoverPrimary: layoutStore.themeColor, // 鼠标经过背景颜色
        colorPressedPrimary: layoutStore.themeColor, // 按下背景颜色
        textColorPrimary: 'white', // 文字颜色
        textColorHoverPrimary: 'white', // 鼠标经过文字颜色
        textColorPressedPrimary: 'white', // 按下文字颜色
        textColorFocusPrimary: 'white', // 聚焦文字颜色
        borderPressedPrimary: layoutStore.themeColor, // 鼠标按下边框颜色

        // default类型按钮
        textColorPressed: layoutStore.themeColor, // 鼠标按下文字颜色
        borderPressed: layoutStore.themeColor, // 鼠标按下边框颜色

        // text类型按钮
        textColorTextPressed: layoutStore.themeColor, // 鼠标按下文字颜色
        textColorTextPressedPrimary: layoutStore.themeColor, // 鼠标按下按钮文字颜色（文本Primary按钮）
      },
      Card: {
        borderRadius: '10px', // 边框圆角
        paddingMedium: '20px', // 内容边距
        titleFontSizeMedium: '16px', // 标题字体大小
      },
      Tabs: {
        panePaddingMedium: '20px 0px 0px 0px'
      },
      Switch : {
        railColorActive: layoutStore.themeColor,
      },
      Scrollbar: {
        height: '7px',
        width: '7px'
      }
    }
  });

  /**
   * 主题
   */
  const theme: ComputedRef = computed(() => {
    return layoutStore.theme == ThemeMode.DARK ? darkTheme : null
  });

  /**
   * 组件加载
   */
  onMounted(() => {
    onScreenResize()
    window.addEventListener('resize', onScreenResize)
  });

  /**
   * 组件卸载
   */
  onBeforeUnmount(() => {
    window.removeEventListener('resize', onScreenResize)
  });

  /**
   * 屏幕大小改变
   */
  function onScreenResize() {
    const width = document.body.clientWidth
    if (width <= 768) {
      layoutStore.deviceType = DeviceType.MOBILE;
      layoutStore.isCollapse = true;
    } else if (width < 992 && width > 768) {
      layoutStore.deviceType = DeviceType.PAD;
      layoutStore.isCollapse = true;
    } else {
      layoutStore.deviceType = DeviceType.PC;
      layoutStore.isCollapse = false;
    }
  }

  /**
   * 关闭菜单
   */
  function closeMenu() {
    layoutStore.isCollapse = true;
  }
</script>

<style lang="scss">
  .vaw-layout-container {
    max-width: 100%;
    overflow-x: hidden;
    min-height: 100vh;
    .mobile-shadow {
      display: none;
    }
  }

  .is-mobile {
    .mobile-shadow {
      background-color: #000000;
      position: fixed;
      top: 0;
      left: 0;
      width: 100vw;
      height: 100vh;
      z-index: 997;
    }
    .close-shadow {
      display: none;
    }
    .show-shadow {
      display: block;
      opacity: 0.5;
      transition: all $transitionTime;
    }
  }
</style>
