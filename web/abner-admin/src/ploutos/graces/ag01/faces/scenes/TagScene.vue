<template>
  <span style="display: none">{{scene.rowData[scene.name]}}</span>
  <n-tag :type="scene.tagType">
    {{scene.rowData[scene.name]}}
  </n-tag>
</template>

<script setup lang="ts">
  import {onBeforeMount, onBeforeUpdate, type PropType} from 'vue'
  import type TagSceneProps from "@/ploutos/graces/ag01/faces/scenes/TagSceneProps.ts";
  import {Parser} from "expr-eval";
  /********************************************************************************
   * tag标签列
   *
   * @author Berlin
   ********************************************************************************/

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    scene: {
      type: Object as PropType<TagSceneProps>,
      required: true
    }
  });

  /**
   * 组件加载前
   */
  onBeforeMount(() => {
    tagType();
  });

  /**
   * 组件更新前
   */
  onBeforeUpdate(() => {
    tagType();
  });

  /**
   * 计算标签类型
   */
  function tagType() {
    if (props.scene.success && Parser.parse(props.scene.success).evaluate(props.scene.rowData)) {
      props.scene.tagType = 'success';
      return;
    }
    if (props.scene.warning && Parser.parse(props.scene.warning).evaluate(props.scene.rowData)) {
      props.scene.tagType = 'warning';
      return;
    }
    if (props.scene.error && Parser.parse(props.scene.error).evaluate(props.scene.rowData)) {
      props.scene.tagType = 'error';
      return;
    }
    props.scene.tagType = 'primary';
  }
</script>