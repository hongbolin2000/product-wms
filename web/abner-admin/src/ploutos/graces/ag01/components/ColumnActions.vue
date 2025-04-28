<template>
  <n-space :size="0" style="justify-content: center;align-items: center">
    <div v-for="(column, index) of columnActions" :key="column.name" style="display: flex;align-items: center">
      <n-divider vertical v-if="index != 0" />
      <component :is="() => renderColumn({...column})"/>
    </div>

    <n-divider vertical v-if="contextMenuOptions.length > 0"/>
    <n-dropdown trigger="hover" :options="contextMenuOptions" v-if="contextMenuOptions.length > 0">
      <n-button text icon-placement="right">
        <template #icon>
          <n-icon class="tip">
            <ChevronDown style="font-size: 14px"/>
          </n-icon>
        </template>
        更多
      </n-button>
    </n-dropdown>
  </n-space>
</template>

<script setup lang="ts">
  import {onMounted, type PropType, shallowRef, type VNode} from "vue";
  import {ChevronDown} from "@vicons/ionicons5";
  import {NIcon} from "naive-ui";
  /********************************************************************************
   * 表格列操作按钮
   *
   * @author Berlin
   ********************************************************************************/
  import type AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";
  import ColumnFactories from "@/ploutos/graces/ag01/faces/ColumnFactories.ts";
  import type Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";

  /**
   * 更多选项
   */
  const contextMenuOptions = shallowRef([]);

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
    },
    datatable: {
      type: Object as PropType<Datatable>,
      required: true
    }
  });

  /**
   * 组件加载
   */
  onMounted(() => {
    const menuOptions = [];
    props.datatable.columns.filter(i => ColumnFactories.isLink(i) && i.option).forEach(column => {
      menuOptions.push({
        key: 'more',
        type: 'render',
        render: () => {
          column.rowData = props.rowData;
          column.datatableTitle = props.datatable.title;
          return ColumnFactories.getInstance().create({...column});
        },
      });
    });
    contextMenuOptions.value = menuOptions;
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
  :deep(.n-button__icon) {
    margin-top: 0 !important;
    margin-bottom: 0 !important;
    margin-left: 0 !important;
  }
</style>