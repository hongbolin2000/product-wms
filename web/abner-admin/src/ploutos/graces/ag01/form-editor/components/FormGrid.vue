<template>
    <n-grid :cols="24 / editorCount" :x-gap="isDrawer ? 0 : 20">
      <n-form-item-gi
          :span="24 / editorCount / editor.spans * widget.spans"
          :label="widget.title"
          v-for="widget of editor.widgets"
          :path="widget.name"
          :key="widget.name"
      >
        <component :is="editorWidget" :widget="widget"/>
      </n-form-item-gi>
    </n-grid>
</template>

<script setup lang="ts">
  /********************************************************************************
   * 编辑表单
   *
   * @author Berlin
   ********************************************************************************/
  import type {PropType} from "vue";
  import type FormEditor from "@/ploutos/graces/ag01/faces/FormEditor.ts";
  import type AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget.ts";
  import WidgetFactories from "@/ploutos/graces/ag01/faces/WidgetFactories.ts";

  /**
   * 父组件传入属性
   */
  const props = defineProps({
    editor: {
      type: Object as PropType<FormEditor>,
      required: true
    },
    formValue: {
      type: Object,
      required: true
    },
    editorCount: {
      type: Number,
      required: true
    },
    isDrawer: {
      type: Boolean,
      required: true,
    }
  });

  /**
   * 生成输入控件
   */
  function editorWidget(prop: {widget: AbstractWidget}) {
    prop.widget.rowData = props.formValue;
    prop.widget.widgets = props.editor.widgets;
    return WidgetFactories.getInstance().create(prop.widget);
  }
</script>

<style scoped lang="scss">

</style>