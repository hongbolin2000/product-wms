<template>
  <n-upload
      :file-list="fileList"
      :list-type="scene.mode == 'text' ? 'text' : 'image-card'"
      :file-list-style="scene.mode == 'text' ? 'line-height: 0' : ''"
      :show-trigger="false"
      :show-remove-button="false"
      :show-download-button="scene.mode != 'text'"
      @download="handleDownload"
      @preview="handlePreview"
      v-if="props.scene.rowData[props.scene.name]"
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
  import type FileSceneProps from "@/ploutos/graces/ag01/faces/scenes/FileSceneProps.ts";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    scene: {
      type: Object as PropType<FileSceneProps>,
      required: true
    }
  });

  /**
   * 组件加载
   */
  const fileList = computed(() => {
    const value: string = props.scene.rowData[props.scene.name];
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
    const value: string = props.scene.rowData[props.scene.name];
    location.href = http.basePath + '/doc/download?file=' + value;
    return false;
  }

  /**
   * 图片预览
   */
  function handlePreview(file: any, detail: { event: MouseEvent }) {
    // 文件下载/图片预览
    detail.event.preventDefault();
    if (props.scene.mode == 'text') {
      handleDownload();
    } else {
      const value: string = props.scene.rowData[props.scene.name];
      window.open(http.basePath + '/doc/image?file=' + value);
    }
  }
</script>