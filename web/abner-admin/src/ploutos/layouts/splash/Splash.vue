<template>
  <div class="splash-wrapper">
    <img src="/loading.gif" alt="loading">
  </div>
</template>

<script setup lang="ts">
  import {onMounted} from 'vue';
  import {useRouter, useRoute} from 'vue-router'
  /********************************************************************************
   * 应用开屏页面
   *
   * @author Berlin
   ********************************************************************************/
  import {http, notification, dialog, message} from "@/ploutos";

  /**
   * 路由
   */
  const router = useRouter();
  const route = useRoute();

  /**
   * 组件加载
   */
  onMounted(async () => {
    // 初始化http客户端
    http.init({
      baseURL: import.meta.env.VITE_API_BASE_URL
    });

    // 初始化通知消息组件
    notification.init();
    dialog.init();
    message.init();

    // 检查用户是否已登录
    const response = await http.post("/auth/isLogin");
    if (!response.data.login) {
      if (response.data.frozen) {
        dialog.warning({
          closable: false,
          content: '用户认证已失效，需重新登录！',
          noCancel: true,
          onConfirmClick: () => {
            router.replace("/login");
          }
        });
      } else {
        setTimeout(() => {
          router.replace("/login");
        }, 500);
      }
      return;
    }

    // 跳转页面
    setTimeout(() => {
      router.replace("/");
    }, 500);
  });
</script>

<style scoped lang="scss">
  .splash-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100vh;
    background-color: #DFFEF4FF;
    img {
      height: 90vh;
    }
  }
</style>
