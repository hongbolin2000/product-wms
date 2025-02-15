<template>
  <div class="exception-container">
    <div class="img-wrapper">
      <img :src="statusImage" :alt="status.toString()"/>
    </div>

    <div class="title">
      <div>{{ statusTip }}</div>
      <div class="margin-top-lg">
        <n-button type="primary" size="small" @click="back" round ghost> 返回上一页 </n-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import {computed} from "vue";
  import {useRouter} from "vue-router";
  /********************************************************************************
   * 异常结果页面
   *
   * @author Berlin
   ********************************************************************************/
  import i_404 from '@/ploutos/layouts/exception/images/404.png';
  import i_403 from '@/ploutos/layouts/exception/images/403.png';
  import i_500 from '@/ploutos/layouts/exception/images/500.png';

  /**
   * 路由对象
   */
  const router = useRouter();

  /**
   * 父控件传入的属性
   */
  const props = defineProps({

    /**
     * 异常状态
     */
    status: {
      type: [String, Number],
      default: 404
    }
  });

  /**
   * 异常状态显示的图片
   */
  const statusImage = computed(() => {
    if (props.status.toString() === '404') {
      return i_404;
    } else if (props.status.toString() === '403') {
      return i_403
    } else {
      return i_500
    }
  });

  /**
   * 异常状态提示文字
   */
  const statusTip = computed(() => {
    if (props.status.toString() === '404') {
      return "您访问的页面不存在~";
    } else if (props.status.toString() === '403') {
      return "您没有权限访问该页面~";
    } else {
      return "您访问的页面出错了~";
    }
  });

  /**
   * 回到首页
   */
  function back() {
    router.go(-1);
  }
</script>

<style scoped lang="scss">
  .exception-container {
    text-align: center;
    height: 100%;
    .img-wrapper {
      width: 80%;
      margin: 0 auto;
      & > img {
        width: 100%;
      }
    }
    .title {
      font-size: 20px;
      color: var(--primary-color);
    }
  }
</style>
