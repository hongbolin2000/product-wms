<template>
  <n-notification-provider>
    <n-message-provider>
      <router-view />
    </n-message-provider>
  </n-notification-provider>
</template>

<script setup lang="ts">
  import {onMounted} from 'vue';
  /********************************************************************************
   * APP启动页
   *
   * @author Berlin
   ********************************************************************************/
  import {menus} from '@/router/menus';
  import {helper} from '@/layouts/helps/layout-helper';

  /**
   * 组件加载
   */
  onMounted(() => {

    // 初始化
    helper.initial({
      menus: menus,
      websiteTitle: '门店数据采集平台'
    });
  });
</script>
