<template>
  <div class="vaw-avatar-container">
    <n-dropdown trigger="hover" :options="options" size="large" @select="handleSelect">
      <div class="action-wrapper">
        <div class="avatar">
          <n-avatar circle size="small" :src="Avatar" />
        </div>

        <span class="nick-name">
          超级管理员
          <n-icon class="tip">
            <CaretDownSharp />
          </n-icon>
        </span>
      </div>
    </n-dropdown>
  </div>
</template>

<script setup lang="ts">
  import { h } from 'vue'
  import {NIcon, useDialog, useLoadingBar, useMessage} from 'naive-ui'
  import { Menu, LogInOutline, CaretDownSharp } from '@vicons/ionicons5'
  import {useRouter} from "vue-router";
  /********************************************************************************
   * 头像
   *
   * @author Berlin
   ********************************************************************************/
  import Avatar from './avatar.gif';
  import {http} from "@/index";
  import {TOKEN_NAME} from '@/layouts/types';

  /**
   * 信息弹框
   */
  const dialog = useDialog();
  const message = useMessage();
  const loadingBar = useLoadingBar();

  /**
   * 路由
   */
  const router = useRouter();

  /**
   * 头像选项
   */
  const options = [
    {
      label: '个人中心',
      key: 'personal-center',
      icon: () =>
        h(NIcon, null, {
          default: () => h(Menu),
        }),
    },
    {
      label: '退出登录',
      key: 'logout',
      icon: () =>
        h(NIcon, null, {
          default: () => h(LogInOutline),
        }),
    },
  ]

  /**
   * 选项操作
   */
  function handleSelect(key: string) {
    if (key == 'personal-center') {
      router.push('/personal/info');
    }

    if (key == 'logout') {
      logout();
    }
  }

  /**
   * 退出登录
   */
  function logout() {
    dialog.warning({
      title: '提示',
      content: '是否要退出当前账号？',
      positiveText: '退出',
      negativeText: '再想想',
      onPositiveClick: () => {
        (async () => {
          let tooltip = message.loading("退出登录中...", {duration: 0});

          // 调用后台退出登录
          await http.post("/auth/logout");
          tooltip.destroy();
          tooltip = message.success("退出登录成功，即将退出系统");

          setTimeout(() => {
            tooltip.destroy();
            loadingBar.start();

            setTimeout(() => {
              loadingBar.finish();
              localStorage.removeItem(TOKEN_NAME);
              router.replace("/login");
            }, 1000);
          }, 1000);
        })();
      },
    })
  }
</script>

<style scoped lang="scss">
  .vaw-avatar-container {
    .action-wrapper {
      display: flex;
      align-items: center;
      .avatar {
        width: calc(#{$logoHeight} - 15px);
        height: calc(#{$logoHeight} - 15px);
        display: flex;
        align-items: center;
        & > img {
          border: 1px solid #f6f6f6;
          width: 100%;
          height: 100%;
          object-fit: cover;
          border-radius: 50%;
        }
      }
      .nick-name {
        margin: 0 5px;
        .tip {
          transform: rotate(0);
          transition: transform $transitionTime;
          margin-left: 2px;
        }
      }
    }
  }
  .vaw-avatar-container:hover {
    cursor: pointer;
    color: var(--primary-color);
    .nick-name .tip {
      transform: rotate(180deg);
      transition: transform $transitionTime;
    }
  }
</style>
