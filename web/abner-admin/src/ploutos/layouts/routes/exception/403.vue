<template>
  <div class="exception-wrapper">
    <n-result status="403" title="您没有权限访问该页面~" description="有疑问请尽快联系管理员哦~">
      <template #footer>
        <n-button @click="back">返回上一页</n-button>
      </template>
    </n-result>
  </div>
</template>

<script setup lang="ts">
  import {useRouter} from "vue-router";
  /********************************************************************************
   * 404页面
   *
   * @author Berlin
   ********************************************************************************/

  /**
   * 路由对象
   */
  const router = useRouter();

  /**
   * 回到上一页
   */
  function back() {
    router.go(-1);
  }
</script>

<style lang="scss">
  .exception-wrapper {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
</style>
