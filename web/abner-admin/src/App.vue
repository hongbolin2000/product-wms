<template>
  <n-dialog-provider>
    <n-message-provider>
      <n-notification-provider>
        <n-loading-bar-provider>
          <router-view />
        </n-loading-bar-provider>
      </n-notification-provider>
    </n-message-provider>
  </n-dialog-provider>
</template>

<script setup lang="ts">
  import {onBeforeMount} from 'vue';
  import {useRouter} from 'vue-router';
  import {appHelper} from '@/ploutos';
  /********************************************************************************
   * APP启动页
   *
   * @author Berlin
   ********************************************************************************/
  import packages from '../package.json';
  import '@/icons/iconfont/iconfont.css';

  /**
   * 路由
   */
  const router = useRouter();

  /**
   * 组件加载
   */
  onBeforeMount(() => {
    // 进入开屏页面
    appHelper.version(packages.version);
    router.replace("/splash");
  });
</script>
