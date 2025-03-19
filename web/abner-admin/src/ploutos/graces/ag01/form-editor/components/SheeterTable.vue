<template>
  <n-collapse-transition :show="!sheeter.collapse">
    <n-data-table
        :columns="renderSheeterColumns(sheeter)"
        :data="sheeter.data"
        size="small"
        :scroll-x="0"
        remote
        :max-height="sheeter.maxHeight"
        :row-props="sheeterRowProps"
    />
  </n-collapse-transition>
</template>

<script setup lang="ts">
  /********************************************************************************
   * @author Berlin
   ********************************************************************************/
  import {h, type HTMLAttributes, type PropType} from "vue";
  import type Sheeter from "@/ploutos/graces/ag01/faces/Sheeter.ts";
  import type {TableBaseColumn} from "naive-ui/es/data-table/src/interface";
  import SheeterActions from "@/ploutos/graces/ag01/form-editor/components/SheeterActions.vue";
  import {message} from "@/ploutos";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    sheeter: {
      type: Object as PropType<Sheeter>,
      required: true
    },
    rowIndex: {
      type: Number,
      required: true
    }
  });

  /**
   * 父组件事件
   */
  const emit = defineEmits<{

    /**
     * 表格行双击事件
     */
    (e: 'onDoubleClick', rowIndex: any): void;

    /**
     * 修改按钮点击
     */
    (e: 'onUpdateClick', rowData: any, rowIndex: number): void;
  }>();

  /**
   * 编辑表格行属性
   */
  function sheeterRowProps(rowData: object, rowIndex: number): HTMLAttributes {
    return {
      onDblclick: () => emit('onDoubleClick', rowIndex),
      style: 'cursor: pointer;',
    }
  }

  /**
   * 生成编辑表格列
   */
  function renderSheeterColumns(sheeter: Sheeter): TableBaseColumn[] {
    const columns: TableBaseColumn[] = [];
    sheeter.widgets.forEach(widget => {
      const column: TableBaseColumn = <TableBaseColumn>{};
      column.key = widget.name;
      column.width = widget.width ? widget.width : 150;
      column.resizable = true;
      column.ellipsis = true;
      column.title = widget.title;
      columns.push(column);
    });

    // 操作按钮
    const column: TableBaseColumn = <TableBaseColumn>{};
    column.key = 'option';
    column.title = '操作';
    column.width = sheeter.added ? 100 : 50;
    column.align = 'center';
    column.fixed = 'right';
    column.render = (rowData, index) => {
      return h(SheeterActions, {
        sheeter: sheeter,
        onUpdateClick: () => {
          emit('onUpdateClick', rowData, index);
        },
        onDeleteClick: () => {
          sheeter.data.splice(index, 1);
          const title = props.sheeter.title ? '[ ' + props.sheeter.title + ' ]' : '';
          message.success(title + '删除成功');
        }
      })
    }
    columns.push(column);

    // 列属性
    columns.forEach(column => {
      column.cellProps = (rowData: object, rowIndex: number): HTMLAttributes => {
        if (rowIndex != props.rowIndex) {
          return;
        }
        return {
          style: {
            'background-color': 'var(--n-td-color-hover)'
          }
        }
      }
    });
    return columns;
  }
</script>

<style scoped lang="scss">

</style>