<template>
  <div/>
</template>

<script setup lang="ts">
  import {onMounted} from 'vue';
  import {useRouter, useRoute} from 'vue-router'
  /********************************************************************************
   * 刷新页面
   *
   * @author Berlin
   ********************************************************************************/

  /**
   * 路由
   */
  const router = useRouter();
  const route = useRoute();

  /**
   * 组件加载
   */
  onMounted( () => {
    const redirect: string[] = route.query.redirect!.toString().split("redirect=");
    router.replace(redirect[redirect.length - 1]);
  });
</script>
