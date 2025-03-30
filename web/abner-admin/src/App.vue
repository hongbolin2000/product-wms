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
  import {layoutHelper} from '@/ploutos';
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
    // 初始化网站信息
    layoutHelper.initialWebsite({
      title: '门店设备数据采集平台',
      subtitle: 'Store equipment data collection platform',
      companyName: '上海丞世物联网技术有限公司',
      version: packages.version
    });

    // 进入开屏页面
    router.replace("/splash");
  });
</script>
