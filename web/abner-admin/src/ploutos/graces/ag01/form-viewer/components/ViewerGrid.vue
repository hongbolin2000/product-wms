<template>
  <n-descriptions
      :style="{width: viewer.formWidth, margin: 'auto'}"
      :label-placement="viewer.placement"
      :column="24 / viewerCount"
  >
    <n-descriptions-item
        v-for="scene of viewer.scenes"
        :label="scene.title"
        :span="24 / viewerCount / viewer.spans * scene.spans"
        :key="scene.name"
    >
      <component :is="viewerScene" :scene="scene"/>
    </n-descriptions-item>
  </n-descriptions>
</template>

<script setup lang="ts">
  import type {PropType} from "vue";
  /********************************************************************************
   * 浏览表单
   *
   * @author Berlin
   ********************************************************************************/
  import type FormViewer from "@/ploutos/graces/ag01/faces/FormViewer.ts";
  import type AbstractScene from "@/ploutos/graces/ag01/faces/AbstractScene.ts";
  import SceneFactories from "@/ploutos/graces/ag01/faces/SceneFactories.ts";

  /**
   * 父组件传入属性
   */
  const props = defineProps({
    viewer: {
      type: Object as PropType<FormViewer>,
      required: true
    },
    viewerValue: {
      type: Object,
      required: true
    },
    viewerCount: {
      type: Number,
      required: true
    }
  });

  /**
   * 生成输入控件
   */
  function viewerScene(prop: {scene: AbstractScene}) {
    prop.scene.rowData = props.viewerValue;
    return SceneFactories.getInstance().create(prop.scene);
  }
</script>

<style scoped lang="scss">

</style>