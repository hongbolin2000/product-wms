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
  import {onMounted} from 'vue';
  import {useRouter} from 'vue-router';
  /********************************************************************************
   * APP启动页
   *
   * @author Berlin
   ********************************************************************************/
  import {menus} from '@/router/menus';
  import {layoutHelper} from '@/layouts';
  import packages from '../package.json';

  /**
   * 路由
   */
  const router = useRouter();

  /**
   * 组件加载
   */
  onMounted(() => {

    // 初始化网站信息
    layoutHelper.initialWebsite({
      title: '门店设备数据采集平台',
      subtitle: 'Store equipment data collection platform',
      companyName: '上海丞世物联网技术有限公司',
      version: packages.version
    });

    // 设置菜单
    layoutHelper.initialMenu(menus);

    // 进入开屏页面
    router.replace("/splash?redirect=" + router.options.history.location);
  });
</script>
