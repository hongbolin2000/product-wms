<template>
  <n-drawer v-model:show="opened" placement="right" :auto-focus="false" :width="300">
    <n-drawer-content title="布局设置" closable class="wrapper">
      <n-divider dashed>主题设置</n-divider>
      <n-grid>
        <n-grid-item
          v-for="(item, index) of themeList"
          :key="index"
          :span="12"
          class="example-wrapper"
        >
          <StyleExample
            :checked="item.checked"
            :left-bg="item.leftBg"
            :right-top-bg="item.rightTopBg"
            :right-bottom-bg="item.rightBottomBg"
            :tip-text="item.tipText"
            @click="themeClick(item)"
          />
        </n-grid-item>
      </n-grid>

      <n-divider dashed>菜单栏样式</n-divider>
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

      <n-divider dashed>布局模式</n-divider>
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

      <n-divider dashed>菜单设置</n-divider>
      <div class="setting-item-wrapper">
        <span style="width: 150px">菜单宽度</span>
        <n-input-number v-model:value="layoutStore.sideWidth" size="small" :min="200" :max="400" :step="10">
          <template #suffix>px</template>
        </n-input-number>
      </div>

      <n-divider dashed>页面切换动画</n-divider>
      <div class="setting-item-wrapper">
        <span style="width: 100px">动画效果</span>
        <n-select
          v-model:value="layoutStore.pageAnimate"
          :options="animateOptions"
          @update:value="onAnimateUpdate"
        />
      </div>

      <n-divider dashed>按钮显示</n-divider>
      <div class="setting-item-wrapper">
        <span>搜索</span>
        <n-switch v-model:value="layoutStore.navbar.isShowSearch" />
      </div>
<!--      <div class="setting-item-wrapper">-->
<!--        <span>消息</span>-->
<!--        <n-switch v-model:value="layoutStore.navbar.isShowMessage" />-->
<!--      </div>-->
      <div class="setting-item-wrapper">
        <span>刷新</span>
        <n-switch v-model:value="layoutStore.navbar.isShowRefresh" />
      </div>
      <div class="setting-item-wrapper">
        <span>全屏</span>
        <n-switch v-model:value="layoutStore.navbar.isShowFullScreen" />
      </div>
    </n-drawer-content>
  </n-drawer>
</template>

<script setup lang="ts">
  import {onMounted, ref, watch} from "vue";
  /********************************************************************************
   * 布局配置
   *
   * @author Berlin
   ********************************************************************************/
  import StyleExample from "@/ploutos/layouts/actions/setting/StyleExample.vue";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";
  import {themeList, sideExampleList, layoutExampleList, primaryColorList, animateOptions} from './LayoutSetting';

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

    // 主题
    themeList.value.forEach((it) => {
      it.checked = layoutStore.theme === it.themeId
    });
    // 菜单栏主题
    sideExampleList.value.forEach((it) => {
      it.checked = layoutStore.sideTheme === it.themeId
    });
    // 布局模式
    layoutExampleList.value.forEach((it) => {
      it.checked = layoutStore.layoutMode === it.layoutId
    });
    // 主题颜色
    primaryColorList.value.forEach((it) => {
      it.checked = layoutStore.themeColor === it.value
    });
  });

  /**
   * 切换主题
   */
  async function themeClick(item: never) {
    themeList.value.forEach((it) => {
      it.checked = it === item;
    });

    layoutStore.theme = item.themeId;
    if (item.themeId === 'dark') {
      exampleClick(sideExampleList.value[0])
    } else {
      exampleClick(sideExampleList.value[1])
    }
  }

  /**
   * 切换菜单栏布局样式
   */
  function exampleClick(item: never) {
    sideExampleList.value.forEach((it) => {
      it.checked = it === item;
    });
    layoutStore.sideTheme = item.themeId;
  }

  /**
   * 切换菜单布局模式
   */
  function layoutExampleClick(item: never) {
    layoutExampleList.value.forEach((it) => {
      it.checked = it === item;
    });
    layoutStore.layoutMode = item.layoutId;
  }

  /**
   * 切换主题颜色
   */
  function colorClick(item: never) {
    primaryColorList.value.forEach((it) => {
      it.checked = it === item;
    })
    layoutStore.themeColor = item.value;
  }

  /**
   * 监听菜单宽度变化
   */
  watch(() => layoutStore.sideWidth, (value) => {
    layoutStore.sideWidth = value;
    const r = document.querySelector(':root') as HTMLElement;
    r.style.setProperty('--menu-width', value + 'px');
  });

  /**
   * 切换页面效果
   */
  function onAnimateUpdate(value: string) {
    layoutStore.pageAnimate = value;
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
    .close-wrapper {
      text-align: right;
      font-size: 20px;
    }
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
