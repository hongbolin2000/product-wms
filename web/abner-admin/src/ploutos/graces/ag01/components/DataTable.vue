<template>
  <n-data-table
      :columns="renderColumns(datatable)"
      :data="datatable.data"
      size="small"
      @update:sorter="onSort"
      :pagination="needPagination ? pagination : false"
      :scroll-x="0"
      remote
      :row-props="rowProps"
  />
</template>

<script setup lang="ts">
import {computed, h, type HTMLAttributes, type PropType, provide, ref, type Ref, type VNode} from "vue";
  import {DataTableSortState, type PaginationProps} from "naive-ui";
  /********************************************************************************
   * 数据表格
   *
   * @author Berlin
   ********************************************************************************/
  import type Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";
  import type {TableBaseColumn} from "naive-ui/es/data-table/src/interface";
  import ColumnFactories from "@/ploutos/graces/ag01/faces/ColumnFactories.ts";
  import type AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";
  import HeaderColumn from "@/ploutos/graces/ag01/components/HeaderColumn.vue";
import LabelColumnFactory from "@/ploutos/graces/ag01/faces/columns/LabelColumnFactory.ts";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    datatable: {
      type: Object as PropType<Datatable>,
      required: true
    },
    needPagination: {
      type: Boolean,
      required: false,
    },
  });

  /**
   * 父组件事件
   */
  const emit = defineEmits<{

    /**
     * 排序
     */
    (e: 'onSort', sorter: {name: string | number, order: 'asc' | 'desc'}): void;

    /**
     * 条件查询
     */
    (e: 'onSearch'): void;

    /**
     * 分页
     */
    (e: 'onPagination', pagination: {pageNumber: number, pageSize: number}): void;

    /**
     * 表格行双击事件
     */
    (e: 'doubleClick', rowData: any): void;
  }>();

  /**
   * 当前排序字段
   */
  const currentSorter: Ref<DataTableSortState> = ref(undefined);

  /**
   * 分页总记录数
   */
  const itemCount = computed(() => {
    return props.datatable?.total;
  })

  /**
   * 分页属性
   */
  const pagination: Ref<PaginationProps> = ref({
    page: 1,
    pageSize: 20,
    itemCount: itemCount,
    showSizePicker: true,
    showQuickJumper: true,
    pageSizes: [20, 50, 100, 500],
    onChange: (page: number) => {
      pagination.value.page = page;
      doubleRowIndex.value = -1;

      emit('onPagination', {
        pageNumber: page, pageSize: pagination.value.pageSize!
      });
    },
    onUpdatePageSize: (pageSize: number) => {
      pagination.value.pageSize = pageSize;
      pagination.value.page = 1;
      doubleRowIndex.value = -1;

      emit('onPagination', {
        pageNumber: 1, pageSize: pageSize
      });
    },
    prefix({ itemCount }) {
      return `总记录数 ${itemCount} 条`
    }
  });

  /**
   * 表格行属性
   */
  let doubleRowIndex = ref(-1);
  function rowProps(rowData: object, rowIndex: number): HTMLAttributes {
    return {
      onDblclick: () => {
        doubleRowIndex.value = rowIndex;

        // 定义了双击事件
        if (props.datatable?.doubleClick!) {
          emit('doubleClick', rowData);
        }
      },
      style: 'cursor: pointer;',
    }
  }

  /**
   * 生成表格列
   */
  function renderColumns(datatable: Datatable): TableBaseColumn[] {
    const columns: TableBaseColumn[] = [];

    // 生成表格列
    datatable.columns.forEach(item => {
      const column: TableBaseColumn = <TableBaseColumn>{};
      column.key = item.name;
      column.sorter = true;
      column.width = item.width ? item.width : 100;
      column.resizable = true;
      column.ellipsis = true;

      // 列属性
      column.cellProps = (rowData: object, rowIndex: number): HTMLAttributes => {
        if (rowIndex != doubleRowIndex.value) {
          return;
        }
        return {
          style: {
            'background-color': 'rgb(247,247,250)'
          }
        }
      }

      // 渲染表格标题
      column.title = () => {
        return h(HeaderColumn, {column: item})
      }

      // 表格排序
      if (currentSorter.value && currentSorter.value.columnKey == item.name) {
        column.sortOrder = currentSorter.value.order;
      }

      // 渲染表格数据列
      if (item.type != LabelColumnFactory.TYPE) {
        debugger
        column.render = (rowData, rowIndex) => renderColumn(rowData, rowIndex, {...item});
      }
      columns.push(column);
    });
    return columns;
  }

  /**
   * 生成表格列
   */
  function renderColumn(rowData: any, rowIndex: number, column: AbstractColumn): VNode {
    column.rowData = rowData;
    column.rowIndex = rowIndex;
    return ColumnFactories.getInstance().create(column);
  }

  /**
   * 排序
   */
  function onSort(state: DataTableSortState) {
    currentSorter.value = state;
    doubleRowIndex.value = -1;

    let sorter = null;
    if (state.order) {
      sorter = {
        name: state.columnKey,
        order: state.order == 'ascend' ? 'asc' : 'desc'
      }
    }
    emit('onSort', sorter);
  }

  /**
   * 条件搜索
   */
  function onSearch() {
    doubleRowIndex.value = -1;
    emit('onSearch');
  }
  provide('onSearch', onSearch);
</script>

<style scoped lang="scss">

</style>