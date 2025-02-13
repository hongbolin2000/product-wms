<template>
  <n-config-provider :theme-overrides="themeOverrides">
    <n-el>
      <div class="login-container">
        <div class="left" v-if="!isMobile">
          <img src="./login-bg.jpg" alt="login"/>
          <div class="content-wrapper">
            <div class="logo-wrapper">
              <img src="./logo.png" alt="logo"/>
            </div>

            <div class="title">{{appStore.websiteOption.title}}</div>
            <div class="sub-title">{{appStore.websiteOption.subtitle}}</div>
            <div class="ttppii"> {{appStore.websiteOption.companyName}} </div>
            <div class="bottom-wrapper">System Version · {{appStore.websiteOption.version}}</div>
          </div>
        </div>

        <div class="right" :class="isMobile ? ['is-mobile'] : ''">
          <div v-if="isMobile" style="margin-bottom: 10px">
            <div class="title">{{appStore.websiteOption.title}}</div>
            <div class="sub-title">{{appStore.websiteOption.subtitle}}</div>
          </div>

          <div class="form-wrapper" :style="isMobile ? 'width: 80%;background-color: white' : ''">
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
            <n-input
              v-model:value="captchaValue"
              placeholder="请输入验证码"
              style="margin-top: 10px"
              clearable
              v-if="authProps.captchaVerify"
            >
              <template #suffix>
                <img :src="captchaObject" alt="验证码" @click="generateCaptcha" style="cursor: pointer">
              </template>
            </n-input>
            <span class="error" v-if="errorMessage">{{errorMessage}}</span>

            <div style="margin-top: 20px;display: flex;justify-content: space-between">
              <n-checkbox v-model:checked="userStore.isRememberAccount" color="#fff" v-if="authProps.allowRememberAccount">
                记住账号
              </n-checkbox>
              <n-checkbox v-model:checked="userStore.isRememberPassword" color="#fff" v-if="authProps.allowRememberAccount">
                记住密码
              </n-checkbox>
              <n-checkbox v-model:checked="userStore.isAutoLogin" color="#fff" v-if="authProps.allowAutoLogin">
                7天内自动登录
              </n-checkbox>
            </div>

            <n-button type="primary" class="login" :loading="loading" @click="onLogin">
              登录
            </n-button>
          </div>

          <div class="ttppii" v-if="isMobile" style="display: block;flex: 0;font-size: 25px;margin-top: 50px">
            {{appStore.websiteOption.companyName}}
          </div>
        </div>
      </div>
    </n-el>
  </n-config-provider>
</template>

<script setup lang="ts">
  import {computed, onBeforeUnmount, onMounted, ref, watch} from "vue";
  import {useMessage, useLoadingBar} from 'naive-ui'
  import {useRouter} from 'vue-router'
  /********************************************************************************
   * 登录界面
   *
   * @author Berlin
   ********************************************************************************/
  import useAppStore from "@/layouts/store/app-store";
  import useUserStore from "@/layouts/store/user-store";
  import useLayoutStore from "@/layouts/store/layout-store";
  import {CryptoHelper} from "@/layouts/helps/crypto-helper";
  import http from '@/layouts/axios/http';
  import {TOKEN_NAME} from "@/layouts/types";

  /**
   * 全局应用状态
   */
  const appStore = useAppStore();
  const userStore = useUserStore();
  const layoutStore = useLayoutStore();

  /**
   * 用户名/密码
   */
  const username = ref('');
  const password = ref('');

  /**
   * 验证码
   */
  const captchaValue = ref('');
  const captchaObject = ref('');
  const captchaId = ref('');

  /**
   * 登录配置
   */
  const authProps = ref({
    captchaVerify: true,
    allowAutoLogin: true,
    allowRememberAccount: true,
    allowRememberPassword: true
  });

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
   * 异常提示信息
   */
  const errorMessage = ref('');

  /**
   * 是否手机模式
   */
  const isMobile = ref(false);

  /**
   * 重写主题样式
   */
  const themeOverrides = computed(() => {
    return {
      common: {
        primaryColor: layoutStore.themeColor,
        primaryColorHover: layoutStore.themeColor,
      },
    }
  });

  /**
   * 组件加载
   */
  onMounted(async () => {
    // 认证配置
    const response = await http.get("/auth/properties");
    authProps.value = response.data;

    // 记住账号/密码
    if (userStore.isRememberAccount && authProps.value.allowRememberAccount) {
      username.value = userStore.username;
    }
    if (userStore.isRememberPassword && authProps.value.allowRememberPassword) {
      password.value = CryptoHelper.decrypt(userStore.key, userStore.password);
    }
    if (!authProps.value.allowAutoLogin) {
      userStore.isAutoLogin = false;
    }

    // 生成验证码
    if (authProps.value.captchaVerify) {
      await generateCaptcha();
    }

    onScreenResize();
    window.addEventListener('resize', onScreenResize);
  });

  /**
   * 生成验证码
   */
  async function generateCaptcha() {
    const response = await http.get("/auth/captcha", {
      responseType: 'blob'
    });
    captchaObject.value = window.URL.createObjectURL(new Blob([response.data]));
    captchaId.value =  response.headers['captcha-id'];
  }

  /**
   * 组件卸载
   */
  onBeforeUnmount(() => {
    window.removeEventListener('resize', onScreenResize)
  });

  /**
   * 屏幕大小改变
   */
  function onScreenResize() {
    const width = document.body.clientWidth
    isMobile.value = width <= 768;
  }

  /**
   * 登录
   */
  async function onLogin() {

    let tooltip = undefined;
    try {
      // 校验用户名密码
      if (!username.value) {
        errorMessage.value = "请输入用户名";
        return;
      }
      if (!password.value) {
        errorMessage.value = "请输入密码";
        return;
      }
      if (authProps.captchaVerify && !captchaValue.value) {
        errorMessage.value = "请输入验证码";
        return;
      }

      // 密码加密
      const key = CryptoHelper.generate256BitKey();
      const encryptPassword = CryptoHelper.aesEncrypt(key, password.value);

      // 登录提示
      loading.value = true;
      tooltip = message.loading('登录中...', {duration: 0});

      // 请求后台进行登录
      const response = await http.post("/auth/login", {
        username: username.value,
        password: encryptPassword,
        key: key,
        autoLogin: userStore.isAutoLogin,
        captchaId: captchaId.value,
        captchaValue: captchaValue.value
      });

      // 检查是否登录成功
      if (response.data.loginCode != 200) {
        errorMessage.value = response.data.message;
        await generateCaptcha();
        return;
      }

      // 将token存入localStorage
      const token = response.headers[TOKEN_NAME];
      if (token) {
        localStorage.setItem(TOKEN_NAME, token);
      }

      // 关闭提示
      tooltip.destroy();
      tooltip = message.success(
        '登录成功，即将进入系统', {duration: 0}
      );

      // 记住账号/密码
      if (userStore.isRememberAccount) {
        userStore.username = username.value;
      }
      if (userStore.isRememberPassword) {
        userStore.key = key;
        userStore.password = encryptPassword;
      }

      setTimeout(() => {
        tooltip.destroy();

        loadingBar.start();
        setTimeout(() => {
          loadingBar.finish();
          router.replace("/");
        }, 1000);
      }, 1000);
    } finally {
      loading.value = false;
      if (tooltip) {
        tooltip.destroy();
      }
    }
  }

  /**
   * 侦听用户名、密码
   */
  watch(username, () => {
    errorMessage.value = '';
  });
  watch(password, () => {
    errorMessage.value = '';
  });
  watch(captchaValue, () => {
    errorMessage.value = '';
  });

  /**
   * 清除用户名
   */
  userStore.$subscribe((mutation, state) => {
    if (!state.isRememberAccount) {
      userStore.username = '';
    }
  });
</script>

<style scoped lang="scss">
  .login-container {
    display: flex;
    overflow: hidden;
    height: 100vh;

    .title {
      margin-top: 10px;
      color: #ffffff;
      font-weight: bold;
      font-size: 24px;
      text-align: center;
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
        .error {
          color: #d6260b;
          margin-left: 10px;
          font-size: 12px;
        }
      }
    }
    .is-mobile {
      background-image: url('./login-bg.jpg');
      background-size: cover;
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
