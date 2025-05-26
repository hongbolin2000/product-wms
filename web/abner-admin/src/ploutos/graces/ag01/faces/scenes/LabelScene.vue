<template>
  <span @click="onTextClick" class="label">{{scene.rowData[scene.name]}}</span>
</template>

<script setup lang="ts">
  import {type PropType} from 'vue'
  import clipboard3 from  'vue-clipboard3'
  /********************************************************************************
   * 文本显示控件
   *
   * @author Berlin
   ********************************************************************************/
  import type AbstractScene from "@/ploutos/graces/ag01/faces/AbstractScene.ts";
  import {message} from "@/ploutos";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    scene: {
      type: Object as PropType<AbstractScene>,
      required: true
    }
  });

  /**
   * 剪切板
   */
  const { toClipboard } = clipboard3();

  /**
   * 文本点击
   */
  async function onTextClick() {
    await toClipboard(props.scene.rowData[props.scene.name]);
    message.success("复制成功");
  }
</script>

<style scoped lang="scss">
  .label {
    &:hover {
      color: var(--primary-color);
    }
    cursor: pointer;
  }
</style>