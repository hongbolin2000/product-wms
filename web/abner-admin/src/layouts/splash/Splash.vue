<template>
  <div class="first-loading-wrp">
    <img src="./loading.gif" alt="loading">
  </div>
</template>

<script setup lang="ts">
  import {onMounted} from 'vue';
  import {http} from "@/layouts/index";
  import {useRouter, useRoute} from 'vue-router'
  import {useDialog} from "naive-ui";
  /********************************************************************************
   * 应用开屏页面
   *
   * @author Berlin
   ********************************************************************************/
  import {message} from "@/layouts";

  /**
   * 路由
   */
  const router = useRouter();
  const route = useRoute();
  const dialog = useDialog();

  /**
   * 组件加载
   */
  onMounted(async () => {
    // 初始化http客户端
    http.init({
      baseURL: 'http://127.0.0.1:8081'
    });

    // 初始化通知消息
    message.init();

    // 检查用户是否已登录
    const response: boolean = await http.post("/auth/isLogin");
    if (!response.data.login) {
      if (response.data.frozen) {
        dialog.warning({
          title: '提示',
          content: '长时间未操作系统，需重新登录！',
          positiveText: '确认',
          onPositiveClick: async () => {
            await router.replace("/login");
          }
        })
      } else {
        await router.replace("/login");
      }
      return;
    }

    // 跳转页面
    setTimeout(() => {
      const redirect: string[] = route.query.redirect.split("redirect=");
      router.replace(redirect[redirect.length - 1]);
    }, 1000);
  });
</script>

<style scoped lang="scss">
  .first-loading-wrp {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100vh;
    background-color: #DFFEF4FF;
    img {
      height: 90vh;
    }
    .title{
      font-size: 28px;
      font-weight: bold;
      text-align: center;
      margin-top: 50px;
    }
  }
</style>
