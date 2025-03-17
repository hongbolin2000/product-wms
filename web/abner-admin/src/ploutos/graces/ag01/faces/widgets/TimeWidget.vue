<template>
  <n-time-picker v-model:formatted-value="widget.rowData[widget.name]" clearable style="width: 100%"/>
</template>

<script setup lang="ts">
  import {onBeforeUpdate, onBeforeMount, type PropType} from 'vue'
  /********************************************************************************
   * 时间输入控件
   *
   * @author Berlin
   ********************************************************************************/
  import {WidgetUtil} from "@/ploutos/graces/ag01/faces/widgets/WidgetUtil.ts";
  import type AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget.ts";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    widget: {
      type: Object as PropType<AbstractWidget>,
      required: true
    }
  });

  /**
   * 组件加载前
   */
  onBeforeMount(() => {
    // 缺省值
    let value = props.widget.rowData[props.widget.name];
    props.widget.rowData[props.widget.name] = !value ? null : value;

    WidgetUtil.disabled(props.widget);
  });

  /**
   * 组件更新前
   */
  onBeforeUpdate(() => {
    WidgetUtil.disabled(props.widget);
  });
</script>

<style scoped lang="scss">

</style>