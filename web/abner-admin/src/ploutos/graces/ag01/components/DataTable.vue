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
      :max-height="maxHeight"
  />

  <n-dropdown
    :x="x" :y="y"
    trigger="manual"
    placement="right-start"
    :show="showContextMenu"
    :options="contextMenuOptions"
    :on-clickoutside="onCloseContextMenu"
  />

  <n-modal
      v-model:show="showModal"
      :title="selectColumn.title"
      :draggable="{bounds: 'none'}"
      :style="{width: selectColumn?.dialogWidth}"
      preset="dialog"
  >
    <component :is="component" :value="componentValue" :is-dialog="true" @on-close="showModal = false"/>
  </n-modal>

  <n-drawer v-model:show="showDrawer" :width="400" placement="right">
    <n-drawer-content :title="selectColumn.title" closable>
      <component :is="component" :value="componentValue" :is-drawer="true" @on-close="showDrawer = false"/>
    </n-drawer-content>
  </n-drawer>
</template>

<script setup lang="ts">
import {
  computed,
  h,
  type HTMLAttributes,
  nextTick,
  type PropType,
  provide,
  ref,
  type Ref,
  shallowRef,
  type VNode
} from "vue";
  import {DataTableSortState, type DropdownOption, type PaginationProps} from "naive-ui";
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
  import type LinkColumnProps from "@/ploutos/graces/ag01/faces/columns/LinkColumnProps.ts";
  import LinkColumnFactory from "@/ploutos/graces/ag01/faces/columns/LinkColumnFactory.ts";

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
    },
    maxHeight: {
      type: String
    }
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
    (e: 'onDoubleClick', rowData: any): void;
  }>();

  /**
   * 当前排序字段
   */
  const currentSorter: Ref<DataTableSortState> = ref(undefined);

  /**
   * 右键菜单位置
   */
  const x = ref(0);
  const y = ref(0);

  /**
   * 右键菜单是否显示
   */
  const showContextMenu = ref(false);

  /**
   * 右键菜单选项
   */
  const contextMenuOptions: Ref<DropdownOption[]> = ref([]);

  /**
   * 右键菜单行数据
   */
  let selectRowData: Ref = ref(undefined);

  /**
   * 是否显示模态弹框
   */
  const showModal = ref(false);

  /**
   * 是否显示抽屉
   */
  const showDrawer = ref(false);

  /**
   * 右键选择的列
   */
  const selectColumn: Ref<LinkColumnProps> = ref({});

  /**
   * 右键link执行的组件
   */
  const component = shallowRef(undefined);

  /**
   * 右键link执行传出的值
   */
  const componentValue: Ref = ref(undefined);

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
          emit('onDoubleClick', rowData);
        }
      },
      onContextmenu: (e: MouseEvent) => {
        if (contextMenuOptions.value.length <= 0) {
          return;
        }
        e.preventDefault();

        doubleRowIndex.value = rowIndex;
        showContextMenu.value = false;
        selectRowData.value = rowData;
        nextTick().then(() => {
          showContextMenu.value = true
          x.value = e.clientX
          y.value = e.clientY
        })
      },
      style: 'cursor: pointer;',
    }
  }

  /**
   * 生成表格列
   */
  function renderColumns(datatable: Datatable): TableBaseColumn[] {
    const columns: TableBaseColumn[] = [];
    const menuOptions = [];

    // 生成表格列
    datatable.columns.forEach(item => {

      // 路由列
      if (item.type == LinkColumnFactory.TYPE) {
        item.rowData = selectRowData;
        menuOptions.push({
          key: item.name,
          type: 'render',
          render: () => {
            return ColumnFactories.getInstance().create(item)
          },
        })
        return;
      }

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
            'background-color': 'var(--n-td-color-hover)'
          }
        }
      }

      // 渲染表格标题
      if (item.filter) {
        column.title = () => {
          return h(HeaderColumn, {column: item})
        }
      } else {
        column.title = item.title;
      }

      // 表格排序
      if (currentSorter.value && currentSorter.value.columnKey == item.name) {
        column.sortOrder = currentSorter.value.order;
      }

      // 渲染表格数据列
      if (item.type != LabelColumnFactory.TYPE) {
        column.render = (rowData, rowIndex) => renderColumn(rowData, rowIndex, {...item});
      }
      columns.push(column);
    });
    contextMenuOptions.value = menuOptions;
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

  /**
   * 关闭右键菜单
   */
  function onCloseContextMenu() {
    showContextMenu.value = false;
  }
  provide('onCloseContextMenu', onCloseContextMenu);

  /**
   * 打开模态弹框
   */
  function onShowModal(column: LinkColumnProps, components: any, value: string) {
    showModal.value = true;
    selectColumn.value = column;
    component.value = components;
    componentValue.value = value;
    onCloseContextMenu();
  }
  provide('onShowModal', onShowModal);

  /**
   * 打开抽屉
   */
  function onShowDrawer(column: LinkColumnProps, components: any, value: string) {
    showDrawer.value = true;
    selectColumn.value = column;
    component.value = components;
    componentValue.value = value;
    onCloseContextMenu();
  }
  provide('onShowDrawer', onShowDrawer);
</script>

<style scoped lang="scss">
  :deep(.n-data-table-th__ellipsis) {
    max-width: 100% !important;
  }
</style>