<template>
  <n-input
      :type="widget.mode"
      v-model:value="widget.rowData[widget.name]"
      :placeholder="'输入' + widget.title"
      :disabled="widget.isDisabled"
      clearable
      :maxlength="widget.maxLength"
      show-count
      show-password-on="mousedown"
      :input-props="{autocomplete: false}"
  >
    <template #prefix v-if="widget.prefix">
      <SvgIcon :name="props.widget.prefixIcon" v-if="props.widget.prefixIcon"/>
      <span v-else>{{props.widget.prefix}}</span>
    </template>

    <template #suffix v-if="widget.suffix">
      <SvgIcon :name="props.widget.suffixIcon" v-if="props.widget.suffixIcon"/>
      <span v-else>{{props.widget.suffix}}</span>
    </template>
  </n-input>
</template>

<script setup lang="ts">
  import {onBeforeUpdate, onBeforeMount, type PropType} from 'vue'
  /********************************************************************************
   * 文本输入控件
   *
   * @author Berlin
   ********************************************************************************/
  import {WidgetUtil} from "@/ploutos/graces/ag01/faces/widgets/WidgetUtil.ts";
  import type TextWidgetProps from "@/ploutos/graces/ag01/faces/widgets/TextWidgetProps.ts";
  import SvgIcon from "@/ploutos/layouts/icons/SvgIcon.vue";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    widget: {
      type: Object as PropType<TextWidgetProps>,
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