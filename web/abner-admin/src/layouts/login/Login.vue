<template>
  <n-el>
    <div class="login-container">
      <div class="left">
        <img src="./login-bg.jpg" alt="login"/>
        <div class="content-wrapper">
          <div class="logo-wrapper">
            <img src="./logo.png" alt="logo"/>
          </div>

          <div class="title">门店设备数据采集平台</div>
          <div class="sub-title">Store equipment data collection platform</div>
          <div class="ttppii"> 上海丞世物联网技术有限公司 </div>
          <div class="bottom-wrapper">System Version · v1.0.0</div>
        </div>
      </div>

      <div class="right">
        <div class="form-wrapper">
          <div class="form-title">用户登录</div>
          <n-input
            v-model:value="username"
            placeholder="请输入用户名"
            clearable
          />
          <n-input
            v-model:value="password"
            placeholder="请输入密码"
            type="password"
            show-password-on="mousedown"
            style="margin-top: 10px"
          />

          <div style="margin-top: 20px;display: flex;justify-content: space-between">
            <n-checkbox v-model:checked="isRemember" color="#fff">记住账号</n-checkbox>
            <n-checkbox v-model:checked="isAutoLogin" color="#fff">自动登录</n-checkbox>
          </div>

          <n-button type="primary" class="login" :loading="loading" @click="onLogin">
            登录
          </n-button>
        </div>
      </div>
    </div>
  </n-el>
</template>

<script setup lang="ts">
  import {ref} from "vue";
  import { useMessage, useLoadingBar } from 'naive-ui'
  import {useRouter} from 'vue-router'
  /********************************************************************************
   * 登录组件
   *
   * @author Berlin
   ********************************************************************************/

  /**
   * 用户名/密码
   */
  const username = ref('');
  const password = ref('');

  /**
   * 是否登录中
   */
  const loading = ref(false);

  /**
   * 提示消息
   */
  const message = useMessage();
  const loadingBar = useLoadingBar();

  /**
   * 路由对象
   */
  const router = useRouter();

  /**
   * 记住账号/自动登录
   */
  const isRemember = ref(false);
  const isAutoLogin = ref(false);

  /**
   * 登录
   */
  function onLogin() {
    loading.value = true;
    let tooltip = message.loading('登录中...', {duration: 0});

    setTimeout(() => {
      loading.value = false;
      tooltip.destroy();

      tooltip = message.success('登录成功，即将进入系统', {duration: 0});
      setTimeout(() => {
        tooltip.destroy();

        loadingBar.start();
        setTimeout(() => {
          loadingBar.finish();
          router.replace("/");
        }, 1000);
      }, 1000);
    }, 1000);
  }
</script>

<style scoped lang="scss">
  .login-container {
    display: flex;
    overflow: hidden;
    height: 100vh;

    .left {
      position: relative;
      min-width: 50%;
      width: 50%;
      & > img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
      &::after {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: rgba(0, 0, 0, 0.6);
      }
      .content-wrapper {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        z-index: 9;
        display: flex;
        flex-direction: column;
        align-items: center;
        .logo-wrapper {
          width: 200px;
          margin-top: 30%;
          & > img {
            width: 100%;
            height: auto;
          }
        }
        .title {
          margin-top: 10px;
          color: #ffffff;
          font-weight: bold;
          font-size: 24px;
        }
        .sub-title {
          margin-top: 10px;
          color: #f5f5f5;
          font-size: 16px;
        }
        .ttppii {
          display: flex;
          flex: 1 1 0;
          justify-content: center;
          align-items: center;
          color: #ffffff;
          font-size: 30px;
          animation: left-to-right 1s cubic-bezier(0.175, 0.885, 0.32, 1.275);
          text-shadow: 0 0 5px var(--primary-color), 0 0 15px var(--primary-color),
          0 0 50px var(--primary-color), 0 0 150px var(--primary-color);
        }
        .bottom-wrapper {
          margin-bottom: 5%;
          color: #c0c0c0;
          font-size: 16px;
        }
      }
    }
    .right {
      width: 100%;
      display: flex;
      justify-content: center;
      flex-direction: column;
      align-items: center;
      .form-wrapper {
        width: 50%;
        max-width: 500px;
        padding: 30px;
        box-shadow: 0 0 7px #dddddd;
        .form-title {
          font-size: 26px;
          margin-bottom: 20px;
          font-weight: bold;
        }

        .login {
          margin-top: 20px;
          width: 100%;
        }
      }
    }
  }
  @keyframes left-to-right {
    from {
      transform: translateX(-100%);
    }
    to {
      transform: translateX(0);
    }
  }
</style>
