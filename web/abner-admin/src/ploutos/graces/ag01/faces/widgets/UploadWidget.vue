<template>
  <span style="display: none">{{widget.rowData[widget.name]}}</span>
  <n-upload
      :action="http.basePath + '/doc/upload'"
      :file-list="fileList"
      :list-type="listType"
      :data="{
        group: widget.group
      }"
      :headers="headers"
      @finish="handleFinish"
      @change="handleChange"
      @download="handleDownload"
      @preview="handlePreview"
      :accept="widget.accept"
      show-download-button
      :disabled="widget.isDisabled"
      :show-trigger="!widget.isDisabled"
  >
    <n-button v-if="widget.mode == 'text' || widget.mode == 'image'">
      点击上传
    </n-button>
    <n-upload-dragger v-if="widget.mode == 'dragger'">
      <div style="margin-bottom: 12px">
        <n-icon size="48" :depth="3">
          <ArchiveIcon />
        </n-icon>
      </div>
      <n-text style="font-size: 16px">
        点击或者拖动文件到该区域来上传
      </n-text>
      <n-p depth="3" style="margin: 8px 0 0 0">
        请不要上传敏感数据，比如你的银行卡号和密码，信用卡号有效期和安全码
      </n-p>
    </n-upload-dragger>
  </n-upload>
</template>

<script setup lang="ts">
  import {onBeforeUpdate, onBeforeMount, type PropType, ref, computed} from 'vue'
  import { ArchiveOutline as ArchiveIcon } from '@vicons/ionicons5'
  import type {UploadFileInfo} from "naive-ui";
  /********************************************************************************
   * 文件上传输入控件
   *
   * @author Berlin
   ********************************************************************************/
  import {WidgetUtil} from "@/ploutos/graces/ag01/faces/widgets/WidgetUtil.ts";
  import type UploadWidgetProps from "@/ploutos/graces/ag01/faces/widgets/UploadWidgetProps.ts";
  import {http, message} from '@/ploutos';

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    widget: {
      type: Object as PropType<UploadWidgetProps>,
      required: true
    }
  });

  /**
   * 传入的header
   */
  const headers = {};
  headers[http.TOKEN_NAME] = http.getToken();

  /**
   * 文件列表
   */
  const fileList = ref<UploadFileInfo[]>([]);

  /**
   * 组件加载前
   */
  onBeforeMount(() => {
    // 加载前已经有了数据
    showFile();
    WidgetUtil.disabled(props.widget);
  });

  /**
   * 组件更新前
   */
  onBeforeUpdate(() => {
    showFile();
    WidgetUtil.disabled(props.widget);
  });

  function showFile() {
    const value: string = props.widget.rowData[props.widget.name];

    // 显示当前图片
    if (value) {
      const fileNames = value.split("/");
      const file: UploadFileInfo = {
        id: value,
        name: fileNames[fileNames.length - 1],
        url: http.basePath + '/doc/image?file=' + value,
        status: 'finished'
      }

      // 用于编辑表单根据基础数据动态展示图片
      if (props.widget.isDisabled) {
        fileList.value = [file];
      }
      if (fileList.value.length <= 0) {
        fileList.value.push(file);
      }
    }
  }

  /**
   * 控件展现模式
   */
  const listType = computed(() => {
    if (props.widget.mode == 'card') {
      return 'image-card';
    }
    if (props.widget.mode == "image") {
      return 'image';
    }
    return 'text';
  });

  /**
   * 文件上传完成
   */
  function handleFinish(options: {file: UploadFileInfo, event?: Event}) {
    const response = (options.event?.target as XMLHttpRequest).response;
    const data = JSON.parse(response);

    // 上传失败
    if (data.code != 200) {
      message.error(props.widget.title + '上传失败！' + data.message);
      options.file.status = 'error';
      return;
    }
    // 上传成功
    options.file.url = http.basePath + '/doc/image?file=' + data.message;
    props.widget.rowData[props.widget.name] = data.message;
    message.success(props.widget.title + '上传完成');
    return options.file;
  }

  /**
   * 文件列表状态改变
   */
  function handleChange(options: {file: UploadFileInfo, fileList: Array<UploadFileInfo>, event?: Event}) {
    // 移除
    if (options.file.status == 'removed') {
      props.widget.rowData[props.widget.name] = '';
      fileList.value = [];
      return;
    }
    // 只允许上传一个文件，除了removed其他状态都更新文件列表
    fileList.value = [options.file];

    // 上传失败
    if (options.file.status == 'error') {
      const response = (options.event?.target as XMLHttpRequest).response;
      try {
        JSON.parse(response);
      } catch (e) {
        message.error(props.widget.title + '上传失败！' + response);
      }
    }
  }

  /**
   * 下载文件
   */
  function handleDownload() {
    const value: string = props.widget.rowData[props.widget.name];
    location.href = http.basePath + '/doc/download?file=' + value;
    return false;
  }

  /**
   * 图片预览
   */
  function handlePreview(file: any, detail: { event: MouseEvent }) {
    // 文件下载/图片预览
    detail.event.preventDefault();
    if (props.widget.mode == 'text' || props.widget.mode == 'dragger') {
      handleDownload();
    } else {
      const value: string = props.widget.rowData[props.widget.name];
      window.open(http.basePath + '/doc/image?file=' + value);
    }
  }
</script>

<style scoped lang="scss">

</style>