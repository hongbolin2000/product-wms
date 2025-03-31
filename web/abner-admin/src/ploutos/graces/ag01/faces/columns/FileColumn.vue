<template>
  <n-upload
      :file-list="fileList"
      :list-type="column.mode == 'text' ? 'text' : 'image-card'"
      :file-list-style="column.mode == 'text' ? 'width: 400px' : 'height: 48px;'"
      :show-trigger="false"
      :show-remove-button="false"
      :show-download-button="column.mode == 'image'"
      @download="handleDownload"
      @preview="handlePreview"
      v-if="props.column.rowData[props.column.name]"
  />
</template>

<script setup lang="ts">
  import {computed, type PropType} from 'vue'
  /********************************************************************************
   * tag标签列
   *
   * @author Berlin
   ********************************************************************************/
  import {http} from '@/ploutos';
  import type FileColumnProps from "@/ploutos/graces/ag01/faces/columns/FileColumnProps.ts";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    column: {
      type: Object as PropType<FileColumnProps>,
      required: true
    }
  });

  /**
   * 组件加载
   */
  const fileList = computed(() => {
    const value: string = props.column.rowData[props.column.name];
    const fileNames = value.split("/");

    return [{
      id: value,
      name: fileNames[fileNames.length - 1],
      url: http.basePath + '/doc/image?file=' + value,
      status: 'finished'
    }]
  });

  /**
   * 下载文件
   */
  function handleDownload() {
    const value: string = props.column.rowData[props.column.name];
    location.href = http.basePath + '/doc/download?file=' + value;
    return false;
  }

  /**
   * 图片预览
   */
  function handlePreview(file: any, detail: { event: MouseEvent }) {
    // 文件下载/图片预览
    detail.event.preventDefault();
    if (props.column.mode == 'text') {
      handleDownload();
    } else {
      const value: string = props.column.rowData[props.column.name];
      window.open(http.basePath + '/doc/image?file=' + value);
    }
  }
</script>

<style lang="scss" scoped>
  :deep(.n-upload-file--image-card-type) {
    height: 48px !important;
  }
  :deep(.n-upload-file-list .n-upload-file) {
    padding: 0;
  }
  :deep(.n-upload-file-list .n-upload-file .n-upload-file-info .n-upload-file-info__name) {
    text-align: left;
  }
</style>