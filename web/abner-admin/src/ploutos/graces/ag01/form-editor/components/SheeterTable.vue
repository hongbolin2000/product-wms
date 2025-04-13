<template>
  <n-collapse-transition :show="!sheeter.collapse">
    <n-data-table
        :columns="renderSheeterColumns(sheeter)"
        :data="sheeter.data"
        size="small"
        :scroll-x="tableWidth"
        :max-height="sheeter.maxHeight"
        :row-props="sheeterRowProps"
        :row-key="rowKey"
        v-model:checked-row-keys="checkedKeys"
        @update-checked-row-keys="handleChecked"
    />
  </n-collapse-transition>
</template>

<script setup lang="ts">
  /********************************************************************************
   * @author Berlin
   ********************************************************************************/
  import {computed, h, type HTMLAttributes, type PropType, type Ref, shallowRef} from "vue";
  import type Sheeter from "@/ploutos/graces/ag01/faces/Sheeter.ts";
  import type {TableBaseColumn, TableColumn, TableSelectionColumn} from "naive-ui/es/data-table/src/interface";
  import SheeterActions from "@/ploutos/graces/ag01/form-editor/components/SheeterActions.vue";
  import {message} from "@/ploutos";
  import SuggestorWidgetFactory from "@/ploutos/graces/ag01/faces/widgets/SuggestorWidgetFactory.ts";
  import UploadWidgetFactory from "@/ploutos/graces/ag01/faces/widgets/UploadWidgetFactory.ts";
  import FileColumn from "@/ploutos/graces/ag01/faces/columns/FileColumn.vue";
  import SelectionWidgetFactory from "@/ploutos/graces/ag01/faces/widgets/SelectionWidgetFactory.ts";
  import {Parser} from "expr-eval";
  import type SelectionWidgetProps from "@/ploutos/graces/ag01/faces/widgets/SelectionWidgetProps.ts";
  import type {DataTableRowKey} from "naive-ui";

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

    /**
     * 选中表格行
     */
    (e: 'onCheckedRows', keys: string[]): void;
  }>();

  /**
   * 表格选择数据的字段key
   */
  const checkRowKey = shallowRef('');

  /**
   * 选中的行key
   */
  const checkedKeys: Ref<DataTableRowKey[]> = shallowRef([]);

  /**
   * 表格总宽度
   */
  const tableWidth = computed(() => {
    let width = 0;
    props.sheeter.widgets.forEach(widget => {
      width += widget.width ? parseInt(widget.width.toString()) : 100;
    });
    return width;
  });

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
   * 返回选择时的Row Key数据
   */
  function rowKey(row: object) {
    return row[checkRowKey.value];
  }

  /**
   * 选中行
   */
  function handleChecked(keys: string[]) {
    emit('onCheckedRows', keys);
  }

  /**
   * 生成编辑表格列
   */
  function renderSheeterColumns(sheeter: Sheeter): TableColumn[] {
    const columns: TableColumn[] = [];

    // 选择列
    sheeter.widgets.filter(i => i.type == SelectionWidgetFactory.TYPE).forEach(item => {
      const selectionWidget: SelectionWidgetProps = <SelectionWidgetProps>{...item};

      const column: TableSelectionColumn = <TableSelectionColumn>{};
      column.type = 'selection';
      column.disabled = (row: any) => {
        if (selectionWidget.disabled) {
          return Parser.parse(selectionWidget.disabled).evaluate(row);
        }
      }
      column.multiple = !selectionWidget.single;
      checkRowKey.value = selectionWidget.name;
      columns.push(column);
    });

    // 表格展示列
    sheeter.widgets.filter(i => !i.hidden).forEach(widget => {
      if (widget.type == SuggestorWidgetFactory.TYPE || widget.type == SelectionWidgetFactory.TYPE) {
        return;
      }

      const column: TableBaseColumn = <TableBaseColumn>{};
      column.key = widget.name;
      column.width = widget.width ? widget.width : 150;
      column.resizable = true;
      column.ellipsis = true;
      column.title = widget.title;

      // 图片
      if (widget.type == UploadWidgetFactory.TYPE) {
        column.render = (rowData) => {
          const column: any = {...widget}
          column.rowData = rowData;
          return h(FileColumn, {column: column});
        }
      }
      columns.push(column);
    });

    // 操作按钮列宽
    let optionWith = 100;
    if (!sheeter.added) {
      optionWith -= 50;
    }
    if (!sheeter.updated) {
      optionWith -= 50;
    }

    // 操作按钮
    if (optionWith > 0) {
      const column: TableBaseColumn = <TableBaseColumn>{};
      column.key = 'option';
      column.title = '操作';
      column.width = optionWith;
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
    }

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