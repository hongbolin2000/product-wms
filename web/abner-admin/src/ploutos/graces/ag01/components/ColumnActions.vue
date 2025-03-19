<template>
  <n-space :size="0" style="justify-content: center;align-items: center">
    <div v-for="(column, index) of columnActions" :key="column.name" style="display: flex">
      <n-divider vertical v-if="index != 0" />
      <component :is="() => renderColumn({...column})"/>
    </div>
  </n-space>
</template>

<script setup lang="ts">
import type {PropType, VNode} from "vue";
  /********************************************************************************
   * @author Berlin
   ********************************************************************************/
  import type AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";
  import ColumnFactories from "@/ploutos/graces/ag01/faces/ColumnFactories.ts";

  /**
   * 父组件传入属性
   */
  const props = defineProps({
    columnActions: {
      type: Object as PropType<AbstractColumn[]>,
      required: true,
    },
    rowData: {
      type: Object,
      required: true,
    },
    rowIndex: {
      type: Number,
      required: true,
    }
  });

  /**
   * 生成表格列
   */
  function renderColumn(column: AbstractColumn): VNode {
    column.rowData = props.rowData;
    column.rowIndex = props.rowIndex;
    return ColumnFactories.getInstance().create(column);
  }
</script>

<style scoped lang="scss">

</style>