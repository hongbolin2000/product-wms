<template>
  <n-date-picker
      v-model:formatted-value="widget.rowData[widget.name]"
      :type="widget.mode"
      style="width: 100%"
      clearable
      :update-value-on-close="'year' != widget.mode && 'week' != widget.mode"
      :value-format="widget.valueFormat"
      :placeholder="'选择' + widget.title"
      :disabled="widget.isDisabled"
  />
</template>

<script setup lang="ts">
  import {onBeforeUpdate, onBeforeMount, type PropType} from 'vue'
  import moment from "moment";
  /********************************************************************************
   * 日期输入控件
   *
   * @author Berlin
   ********************************************************************************/
  import {WidgetUtil} from "@/ploutos/graces/ag01/faces/widgets/WidgetUtil.ts";
  import type DateWidgetProps from "@/ploutos/graces/ag01/faces/widgets/DateWidgetProps.ts";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    widget: {
      type: Object as PropType<DateWidgetProps>,
      required: true
    }
  });

  /**
   * 组件加载前
   */
  onBeforeMount(() => {

    // 格式化
    let valueFormat = '', mode = props.widget.mode;
    switch (mode) {
      case 'date':
      case 'daterange':
      case 'quarter':
      case 'quarterrange':
      case 'week':
        valueFormat = 'yyyy-MM-dd';
        break;
      case 'datetime':
      case 'datetimerange':
        valueFormat = "yyyy-MM-dd HH:mm:ss";
        break;
      case 'month':
      case 'monthrange':
        valueFormat = "yyyy-MM";
        break
      case 'year':
      case 'yearrange':
        valueFormat = 'yyyy';
        break;
      default:
        valueFormat = 'yyyy-MM-dd';
    }
    props.widget.valueFormat = valueFormat;

    // 缺省日期
    let value = props.widget.rowData[props.widget.name];
    if (props.widget.default == 'current') {
      value = moment().format(valueFormat.toUpperCase());
    }
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