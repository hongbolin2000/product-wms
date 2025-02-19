<template>
  <n-drawer v-model:show="opened" :width="350">
    <n-drawer-content title="布局设置" closable class="wrapper" :native-scrollbar="false">
      <n-divider dashed>菜单栏</n-divider>
      <n-grid>
        <n-grid-item
          v-for="(item, index) of sideExampleList"
          :key="index"
          :span="8"
          class="example-wrapper"
        >
          <StyleExample
            :checked="item.checked"
            :left-bg="item.leftBg"
            :right-top-bg="item.rightTopBg"
            :right-bottom-bg="item.rightBottomBg"
            :tip-text="item.tipText"
            @click="exampleClick(item)"
          />
        </n-grid-item>
      </n-grid>

      <n-divider dashed>导航模式</n-divider>
      <n-grid>
        <n-grid-item
          v-for="(item, index) of layoutExampleList"
          :key="index"
          :span="8"
          class="example-wrapper"
        >
          <StyleExample
            :checked="item.checked"
            :left-bg="item.leftBg"
            :right-top-bg="item.rightTopBg"
            :right-bottom-bg="item.rightBottomBg"
            :class="[item.class || '']"
            :tip-text="item.tipText"
            @click="layoutExampleClick(item)"
          />
        </n-grid-item>
      </n-grid>

      <n-divider dashed>主题颜色</n-divider>
      <n-grid class="colors-wrapper">
        <n-grid-item
          v-for="(item, index) of primaryColorList"
          :key="index"
          :span="4"
          class="color-wrapper"
          :class="{ circle: item.checked }"
          :style="{ backgroundColor: item.value }"
          @click="colorClick(item)"
        />
      </n-grid>
    </n-drawer-content>
  </n-drawer>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
/********************************************************************************
 * 布局配置
 *
 * @author Berlin
 ********************************************************************************/
import StyleExample from "./StyleExample.vue";
import useLayoutStore from "@/ploutos/layouts/store/layout-store";
import {
  layoutExampleList,
  type LayoutSelectOption,
  type LayoutStyleOption,
  primaryColorList,
  sideExampleList
} from './LayoutSetting';

/**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

  /**
   * 是否打开
   */
  const opened = ref(false);

  /**
   * 组件加载
   */
  onMounted(() =>{
    // 导航栏主题
    sideExampleList.value.forEach((it) => {
      it.checked = layoutStore.sideTheme == it.sideThemeId;
      setRightBottomBg(it);
    });
    // 布局模式
    layoutExampleList.value.forEach((it) => {
      it.checked = layoutStore.layoutMode == it.layoutId;
      setRightBottomBg(it);
    });
    // 主题颜色
    primaryColorList.value.forEach((it) => {
      it.checked = layoutStore.themeColor == it.value;
    });
  });

  /**
   * 选中时设置背景色
   */
  function setRightBottomBg(it: LayoutStyleOption) {
    if (it.checked) {
      it.rightBottomBg = 'var(--n-resize-trigger-color-hover)';
    } else {
      it.rightBottomBg = '#f5f5f5';
    }
  }

  /**
   * 切换菜单栏布局样式
   */
  function exampleClick(item: LayoutStyleOption) {
    sideExampleList.value.forEach((it) => {
      it.checked = it === item;
      setRightBottomBg(it);
    });
    layoutStore.sideTheme = item.sideThemeId;
  }

  /**
   * 切换菜单布局模式
   */
  function layoutExampleClick(item: LayoutStyleOption) {
    layoutExampleList.value.forEach((it) => {
      it.checked = it === item;
      setRightBottomBg(it);
    });
    layoutStore.layoutMode = item.layoutId;
  }

  /**
   * 切换主题颜色
   */
  function colorClick(item: LayoutSelectOption) {
    primaryColorList.value.forEach((it) => {
      it.checked = it === item;
    })
    layoutStore.themeColor = item.value;
  }

  /**
   * 打开布局设置抽屉
   */
  const openLayoutSettingDrawer = () => {
    opened.value = true;
  }

  /**
   * 暴露方法，提供给父组件调用
   */
  defineExpose({
    openLayoutSettingDrawer
  });
</script>

<style scoped lang="scss">
  .wrapper {
    .colors-wrapper {
      display: flex;
      justify-content: space-evenly;
      align-items: center;
      .color-wrapper {
        width: 20px;
        height: 20px;
        border-radius: 5px;
        border: 1px solid #c1c1c1;
        margin-bottom: 20px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      }
      .circle::after {
        content: '';
        display: block;
        margin: 25px auto 0;
        width: 8px;
        height: 8px;
        border-radius: 50%;
        background-color: rgb(3, 190, 50);
        text-align: center;
      }
    }
    .setting-item-wrapper {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px;
      font-size: 14px;
    }
  }
  .example-wrapper + .example-wrapper {
    margin-bottom: 30px;
  }
</style>
