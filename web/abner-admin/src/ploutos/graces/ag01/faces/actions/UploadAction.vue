<template>
  <n-upload
      :action="http.basePath + action.link"
      @finish="handleFinish"
      :accept="action.accept"
      :headers="headers"
      :show-file-list="false"
      @change="handleChange"
  >
    <n-button :disabled="props.action.isDisabled">
      <template #icon v-if="action.icon">
        <SvgIcon :name="action.icon"/>
      </template>
      {{action.title}}
    </n-button>
  </n-upload>
</template>

<script setup lang="ts">
  import {inject, onBeforeMount, onBeforeUpdate, type PropType} from 'vue'
  import type {UploadFileInfo} from "naive-ui";
  import {Parser} from "expr-eval";
  /********************************************************************************
   * 文件上传动作控件
   *
   * @author Berlin
   ********************************************************************************/
  import type UploadActionProps from "@/ploutos/graces/ag01/faces/actions/UploadActionProps.ts";
  import {http, message, loading} from '@/ploutos';
  import SvgIcon from "@/ploutos/layouts/icons/SvgIcon.vue";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    action: {
      type: Object as PropType<UploadActionProps>,
      required: true
    }
  });

  /**
   * 注入刷新函数
   */
  const onRefresh = inject<Function>('onRefresh');

  /**
   * 传入的header
   */
  const headers = {};
  headers[http.TOKEN_NAME] = http.getToken();

  /**
   * 组件加载前
   */
  onBeforeMount(() => {
    disabled();
  });

  /**
   * 组件更新前
   */
  onBeforeUpdate(() => {
    disabled();
  })

  /**
   * 禁用
   */
  function disabled() {
    if (props.action.rowData && props.action.disabled) {
      props.action.isDisabled = Parser.parse(props.action.disabled).evaluate(props.action.rowData);
    }
  }

  /**
   * 文件上传完成
   */
  function handleFinish(options: {file: UploadFileInfo, event?: Event}) {
    loading(false);
    const response = (options.event?.target as XMLHttpRequest).response;
    const data = JSON.parse(response);

    // 导入失败
    if (data.code != 200) {
      if (data.message) {
        message.error(data.message);
      } else {
        message.error(props.action.title + '失败');
      }
      return;
    }
    // 导入成功
    if (data.message && data.message != 'Success') {
      message.success(data.message);
    } else {
      message.success(props.action.title + '成功');
    }
    onRefresh();
    return options.file;
  }

  /**
   * 文件列表状态改变
   */
  function handleChange(options: {file: UploadFileInfo, fileList: Array<UploadFileInfo>, event?: Event}) {
    // 开始上传
    if (options.file.status == 'uploading') {
      loading(true);
    }
    // 上传失败
    if (options.file.status == 'error') {
      loading(false);
      const error = (options.event?.target as XMLHttpRequest).response;
      message.error(props.action.title + '失败！' + error);
    }
  }
</script>

<style scoped lang="scss">

</style>