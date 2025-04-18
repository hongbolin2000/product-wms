<template>
  <span>{{scene.rowData[scene.name]}}</span>
</template>

<script setup lang="ts">
  import {type PropType} from 'vue'
  /********************************************************************************
   * 文本显示控件
   *
   * @author Berlin
   ********************************************************************************/
  import type AbstractScene from "@/ploutos/graces/ag01/faces/AbstractScene.ts";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    scene: {
      type: Object as PropType<AbstractScene>,
      required: true
    }
  });
</script>

<style scoped lang="scss">

</style>