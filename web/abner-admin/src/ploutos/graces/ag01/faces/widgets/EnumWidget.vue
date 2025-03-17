<template>
  <n-select
      v-if="widget.mode == 'select'"
      v-model:value="widget.rowData[widget.name]"
      :options="widget.options"
      :clearable="!widget.required"
  />
  <n-space :size="10" v-else>
    <n-radio
        v-for="option of widget.options"
        :checked="widget.rowData[widget.name] === option.value"
        :value="option.value"
        :name="option.value"
        @change="handleChange"
    >
      {{option.label}}
    </n-radio>
  </n-space>
</template>

<script setup lang="ts">
  import {onBeforeUpdate, onBeforeMount, type PropType} from 'vue'
  /********************************************************************************
   * 枚举输入控件
   *
   * @author Berlin
   ********************************************************************************/
  import {WidgetUtil} from "@/ploutos/graces/ag01/faces/widgets/WidgetUtil.ts";
  import type EnumWidgetProps from "@/ploutos/graces/ag01/faces/widgets/EnumWidgetProps.ts";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    widget: {
      type: Object as PropType<EnumWidgetProps>,
      required: true
    }
  });

  /**
   * 组件加载前
   */
  onBeforeMount(() => {
    WidgetUtil.disabled(props.widget);
  });

  /**
   * 组件更新前
   */
  onBeforeUpdate(() => {
    WidgetUtil.disabled(props.widget);
  });

  /**
   * 单选值更改
   */
  function handleChange(e: Event) {
    props.widget.rowData[props.widget.name] = (e.target as HTMLInputElement).value
  }
</script>

<style scoped lang="scss">

</style>