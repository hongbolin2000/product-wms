<template>
  <n-config-provider :theme="theme" style="height: 100%" :style="{backgroundColor: layoutStore.themeBgColor}">
    <div class="exception-wrapper">
      <n-result status="404" title="您访问的页面不存在~" description="有疑问请尽快联系管理员哦~">
        <template #footer>
          <n-button @click="back">返回上一页</n-button>
        </template>
      </n-result>
    </div>
  </n-config-provider>
</template>

<script setup lang="ts">
  import {useRouter} from "vue-router";
  import {darkTheme} from 'naive-ui';
  import {computed, type ComputedRef} from "vue";
  import {ThemeMode} from "@/ploutos/layouts/types";
  /********************************************************************************
   * 404页面
   *
   * @author Berlin
   ********************************************************************************/
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";

  /**
   * 路由对象
   */
  const router = useRouter();

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

  /**
   * 主题
   */
  const theme: ComputedRef = computed(() => {
    return layoutStore.theme == ThemeMode.DARK ? darkTheme : null
  });

  /**
   * 回到上一页
   */
  function back() {
    router.go(-1);
  }
</script>

<style lang="scss">
  .exception-wrapper {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
</style>
