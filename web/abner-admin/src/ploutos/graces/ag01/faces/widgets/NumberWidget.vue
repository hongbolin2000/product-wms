<template>
  <n-input-number
      v-model:value="widget.rowData[widget.name]"
      :placeholder="'输入' + widget.title"
      clearable
      :disabled="widget.isDisabled"
      style="width: 100%"
      :min="widget.min"
      :max="widget.max"
      :precision="widget.scale"
      autocomplete="tel"
  >
    <template #prefix v-if="widget.prefix">
      <SvgIcon :name="props.widget.prefixIcon" v-if="props.widget.prefixIcon"/>
      <span v-else>{{props.widget.prefix}}</span>
    </template>

    <template #suffix v-if="widget.suffix">
      <SvgIcon :name="props.widget.suffixIcon" v-if="props.widget.suffixIcon"/>
      <span v-else>{{props.widget.suffix}}</span>
    </template>
  </n-input-number>
</template>

<script setup lang="ts">
  import {onBeforeMount, onBeforeUpdate, type PropType} from 'vue'
  /********************************************************************************
   * 数字输入控件
   *
   * @author Berlin
   ********************************************************************************/
  import {WidgetUtil} from "@/ploutos/graces/ag01/faces/widgets/WidgetUtil.ts";
  import type NumberWidgetProps from "@/ploutos/graces/ag01/faces/widgets/NumberWidgetProps.ts";
  import SvgIcon from "@/ploutos/layouts/icons/SvgIcon.vue";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    widget: {
      type: Object as PropType<NumberWidgetProps>,
      required: true
    }
  });

  /**
   * 组件加载前
   */
  onBeforeMount(() => {
    if (props.widget.prefix.startsWith('icon-')) {
      props.widget.prefixIcon = props.widget.prefix.split('icon-')[1];
    }
    if (props.widget.suffix.startsWith('icon-')) {
      props.widget.suffixIcon = props.widget.suffix.split('icon-')[1];
    }

    // 缺省值
    let value = props.widget.rowData[props.widget.name];
    value = !value ? 0 : parseFloat(value);
    props.widget.rowData[props.widget.name] = value;

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