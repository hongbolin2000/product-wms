<template>
  <n-checkbox
      v-if="widget.mode == 'check'"
      v-model:checked="widget.rowData[widget.name]"
      :checked-value="widget.checked"
      :unchecked-value="widget.unchecked"
   />
  <n-switch
      v-else
      v-model:checked="widget.rowData[widget.name]"
      :checked-value="widget.checked"
      :unchecked-value="widget.unchecked"
  />
</template>

<script setup lang="ts">
  import {onBeforeUpdate, onBeforeMount, type PropType} from 'vue'
  /********************************************************************************
   * 选择输入控件
   *
   * @author Berlin
   ********************************************************************************/
  import {WidgetUtil} from "@/ploutos/graces/ag01/faces/widgets/WidgetUtil.ts";
  import type CheckWidgetProps from "@/ploutos/graces/ag01/faces/widgets/CheckWidgetProps.ts";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    widget: {
      type: Object as PropType<CheckWidgetProps>,
      required: true
    }
  });

  /**
   * 组件加载前
   */
  onBeforeMount(() => {
    // 缺省值
    let value = props.widget.rowData[props.widget.name];
    if (!value) {
      props.widget.rowData[props.widget.name] = props.widget.unchecked;
    }
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