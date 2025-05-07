<template>
  <div class="datatable-action-wrapper" v-if="showTools">
    <!-- 按钮 -->
    <HeaderActions :datatable="datatable" :module="module" :module-name="moduleName" @on-search="onSearch"/>

    <!-- 工具栏 -->
    <HeaderTools :main-data-table="mainDataTable" @on-export-csv="exportCsv"/>
  </div>

  <!-- 服务端数据表 -->
  <n-data-table
      :columns="renderColumns(datatable)"
      :data="datatable.data"
      size="small"
      @update:sorter="onSort"
      :pagination="needPagination ? pagination : false"
      :scroll-x="tableWidth"
      :remote="true"
      :row-props="rowProps"
      :max-height="maxHeight"
      :row-key="rowKey"
      v-model:checked-row-keys="datatable.checkedKeys"
      ref="tableRef"
      :get-csv-cell="getCsvCell"
      :get-csv-header="getCsvHeader"
      :single-line="!mainDataTable.bordered && !layoutStore.bordered"
      :striped="mainDataTable.striped || layoutStore.striped"
      v-if="!static"
      :summary="summaryColumns.length > 0 ? renderSummary : undefined"
  />
  <!-- 静态数据表 -->
  <n-data-table
      v-else
      :columns="renderColumns(datatable)"
      :data="datatable.data"
      size="small"
      :scroll-x="tableWidth"
      :row-props="rowProps"
      :max-height="maxHeight"
      :row-key="rowKey"
      v-model:checked-row-keys="datatable.checkedKeys"
      ref="tableRef"
      :get-csv-cell="getCsvCell"
      :get-csv-header="getCsvHeader"
      :single-line="!mainDataTable.bordered && !layoutStore.bordered"
      :striped="mainDataTable.striped || layoutStore.striped"
      :summary="summaryColumns.length > 0 ? renderSummary : undefined"
  />

  <!-- 表格右键菜单 -->
  <n-dropdown
    :x="x" :y="y"
    trigger="manual"
    placement="right-start"
    :show="showContextMenu"
    :options="contextMenuOptions"
    :on-clickoutside="onCloseContextMenu"
  />

  <!-- dialog操作页面 -->
  <n-modal
      v-model:show="showModal"
      :title="title"
      :draggable="{bounds: 'none'}"
      :style="{width: selectColumn?.dialogWidth}"
      preset="dialog"
      :mask-closable="false"
      :close-on-esc="false"
  >
    <component :is="component" :params="componentParams" :is-dialog="true"
               @on-close="showModal = false" @on-refresh="onSearch" @on-change-title="(value) => title = value"
    />
  </n-modal>

  <!-- drawer操作页面 -->
  <n-drawer v-model:show="showDrawer" :width="selectColumn.drawerWidth" placement="right" :mask-closable="false" :close-on-esc="false">
    <n-drawer-content :title="title" closable :body-content-style="{padding: 0}">
      <component :is="component" :params="componentParams" :is-drawer="true" @on-change-title="(value) => title = value"
                 @on-close="showDrawer = false" @on-refresh="onSearch"
      />
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
    type VNode,
    onBeforeMount
  } from "vue";
  import type {DataTableCreateSummary, DataTableSortState, DropdownOption} from "naive-ui";
  import {Parser} from "expr-eval";
  /********************************************************************************
   * 数据表格
   *
   * @author Berlin
   ********************************************************************************/
  import type Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";
  import type {
    RowData,
    TableBaseColumn,
    TableColumn,
    TableSelectionColumn
  } from "naive-ui/es/data-table/src/interface";
  import ColumnFactories from "@/ploutos/graces/ag01/faces/ColumnFactories.ts";
  import type AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";
  import HeaderColumn from "@/ploutos/graces/ag01/components/HeaderColumn.vue";
  import LabelColumnFactory from "@/ploutos/graces/ag01/faces/columns/LabelColumnFactory.ts";
  import type LinkColumnProps from "@/ploutos/graces/ag01/faces/columns/LinkColumnProps.ts";
  import SelectionColumnFactory from "@/ploutos/graces/ag01/faces/columns/SelectionColumnFactory.ts";
  import type SelectionColumnProps from "@/ploutos/graces/ag01/faces/columns/SelectionColumnProps.ts";
  import TagColumnFactory from "@/ploutos/graces/ag01/faces/columns/TagColumnFactory.ts";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store.ts";
  import ColumnActions from "@/ploutos/graces/ag01/components/ColumnActions.vue";
  import useAppStore from "@/ploutos/layouts/store/app-store.ts";
  import HeaderActions from "@/ploutos/graces/ag01/components/HeaderActions.vue";
  import HeaderTools from "@/ploutos/graces/ag01/components/HeaderTools.vue";

  /**
   * 应用状态
   */
  const appStore = useAppStore();

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    // 主数据表
    mainDataTable: {
      type: Object as PropType<Datatable>,
      required: false
    },
    // 当前数据表
    datatable: {
      type: Object as PropType<Datatable>,
      required: true
    },
    // 是否需要分页
    needPagination: {
      type: Boolean,
    },
    // 表格最大高度
    maxHeight: {
      type: String
    },
    // 是否显示工具栏
    showTools: {
      type: Boolean,
    },
    // 是否静态界面，只加载界面定义，数据由功能设置
    static: {
      type: Boolean
    },
    // 功能模块号
    module: {
      type: String,
      required: true,
    },
    // 通用界面名
    moduleName: {
      type: String,
      required: true,
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
    (e: 'onPagination', pager: {pageNumber: number, pageSize: number}): void;

    /**
     * 表格行双击事件
     */
    (e: 'onDoubleClick', rowData: any): void;
  }>();

  /**
   * 主数据表
   */
  const mainDataTable: Ref = ref(props.mainDataTable);

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();

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
  const selectColumn: Ref<LinkColumnProps> = ref(<LinkColumnProps>{});

  /**
   * 右键link执行的组件
   */
  const component = shallowRef(undefined);

  /**
   * 右键link执行传出的参数
   */
  const componentParams: Ref = ref(undefined);

  /**
   * 表格Ref
   */
  const tableRef = ref();

  /**
   * 新增/编辑时的弹框标题
   */
  const title = shallowRef('');

  /**
   * 总结栏列
   */
  const summaryColumns: Ref<AbstractColumn[]> = shallowRef([]);

  /**
   * 分页器
   */
  const pager = ref({
    pageNumber: 1,
    pageSize: 20
  });

  /**
   * 分页属性
   */
  const pagination = computed(() => {
    return {
      page: pager.value.pageNumber,
      pageSize: pager.value.pageSize,
      itemCount: props.datatable?.total,
      showSizePicker: true,
      showQuickJumper: true,
      pageSizes: [20, 50, 100, 500],
      onChange: (pageNumber: number) => {
        pager.value.pageNumber = pageNumber;
        doubleRowIndex.value = -1;
        props.datatable.checkedKeys = [];

        emit('onPagination', {
          pageNumber: pageNumber, pageSize: pager.value.pageSize!
        });
      },
      onUpdatePageSize: (pageSize: number) => {
        pager.value.pageNumber = 1;
        pager.value.pageSize = pageSize;
        doubleRowIndex.value = -1;
        props.datatable.checkedKeys = [];

        emit('onPagination', {
          pageNumber: 1, pageSize: pageSize
        });
      },
      prefix({ itemCount }) {
        return `总记录数 ${itemCount} 条`
      }
    }
  });

  /**
   * 组件加载
   */
  onBeforeMount(() => {
    if (!mainDataTable.value) {
      mainDataTable.value = props.datatable;
    }
    summaryColumns.value = props.datatable.columns.filter(i => i.summary);
  });

  /**
   * 表格总宽度
   */
  const tableWidth = computed(() => {
    let width = 0;
    props.datatable.columns.forEach(column => {
      width += column.width ? parseInt(column.width.toString()) : 100;

      if (ColumnFactories.isLink(column)) {
        width += 20;
      }
    });
    return width;
  });

  /**
   * 表格行属性
   */
  let doubleRowIndex = ref(-1);
  function rowProps(rowData: object, rowIndex: number): HTMLAttributes {
    return {
      onDblclick: () => {
        doubleRowIndex.value = rowIndex;
        emit('onDoubleClick', rowData);
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
  function renderColumns(datatable: Datatable): TableColumn[] {
    const columns: TableColumn[] = [];
    const menuOptions = [];

    // 右键操作按钮（所有按钮列都能在右键中使用）
    datatable.columns.filter(i => ColumnFactories.isLink(i)).forEach(column => {
      column.rowData = selectRowData;
      column.datatableTitle = datatable.title;
      column.module = props.module;
      column.moduleName = props.moduleName;

      menuOptions.push({
        key: column.name,
        type: 'render',
        render: () => {
          const actionColumn = {...column};
          actionColumn.option = true;
          return ColumnFactories.getInstance().create(actionColumn);
        },
      });
    });
    contextMenuOptions.value = menuOptions;

    // 选择列
    datatable.columns.filter(i => i.type == SelectionColumnFactory.TYPE).forEach(item => {
      const selectionColumn: SelectionColumnProps = <SelectionColumnProps>{...item};

      const column: TableSelectionColumn = <TableSelectionColumn>{};
      column.type = 'selection';
      column.disabled = (row: any) => {
        if (selectionColumn.disabled) {
          return Parser.parse(selectionColumn.disabled).evaluate(row);
        }
      }
      column.multiple = !selectionColumn.single;
      datatable.checkRowKey = selectionColumn.name;
      column.cellProps = (rowData: object, rowIndex: number) => columnProps(rowIndex);
      columns.push(column);
    });

    // 生成表格列
    datatable.columns.forEach(item => {
      if (ColumnFactories.isLink(item) || item.type == SelectionColumnFactory.TYPE) {
        return;
      }

      const column: TableBaseColumn = <TableBaseColumn>{};
      column.key = item.name;
      column.sorter = props.static ? 'default' : true;
      column.width = item.width ? item.width : null;
      column.resizable = true;
      column.ellipsis = TagColumnFactory.TYPE != item.type;
      column.align = item.align

      // 渲染表格标题
      if (item.filter) {
        column.title = () => {
          return h(HeaderColumn, {column: item})
        }
      } else {
        column.title = item.title;
      }
      column.cellProps = (rowData: object, rowIndex: number) => columnProps(rowIndex);

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

    // 展示在表格的操作按钮
    const column: TableBaseColumn = <TableBaseColumn>{width: 0};
    const columnActions = datatable.columns.filter(i => ColumnFactories.isLink(i) && !i.option);
    const optionWidth = datatable.columns.filter(i => i.option).length > 0 ? 60 : 0;
    if (columnActions.length > 0 || optionWidth > 0) {
      columnActions.forEach(item => {
        const width = item.width ? parseInt(item.width.toString()): 80;
        column.width = parseInt(column.width.toString()) + width;
      });

      column.width = parseInt(column.width.toString()) + optionWidth + (columnActions.length * 20);
      column.key = 'option';
      column.title = '操作';
      column.align = 'center';
      column.fixed = 'right';
      column.render = (rowData, rowIndex) => {
        return h(ColumnActions, {
          rowData: rowData, rowIndex: rowIndex, columnActions: columnActions, datatable: datatable
        });
      }
      column.cellProps = (rowData: object, rowIndex: number) => columnProps(rowIndex);
      columns.push(column);
    }

    return columns;
  }

  /**
   * 列属性
   */
  function columnProps(rowIndex: number): HTMLAttributes {
    if (rowIndex != doubleRowIndex.value) {
      return;
    }
    return {
      style: {
        'background-color': 'var(--n-td-color-hover)'
      }
    }
  }

  /**
   * 返回选择时的Row Key数据
   */
  function rowKey(row: object) {
    return row[props.datatable.checkRowKey];
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

    pager.value.pageNumber = 1;
    props.datatable.checkedKeys = [];

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
    props.datatable.checkedKeys = [];
    pager.value.pageNumber = 1;

    doubleRowIndex.value = -1;
    emit('onSearch');
  }
  provide('onSearch', onSearch);
  provide('onRefresh', onSearch);

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
  function onShowModal(column: LinkColumnProps, components: any, params: string) {
    showModal.value = true;
    selectColumn.value = column;
    component.value = components;
    componentParams.value = params;
    onCloseContextMenu();
  }
  provide('onShowModal', onShowModal);

  /**
   * 打开抽屉
   */
  function onShowDrawer(
      column: LinkColumnProps, components: any,
      params: {id: string, module: string, name: string}
  ) {
    showDrawer.value = true;
    selectColumn.value = column;
    component.value = components;
    componentParams.value = params;
    onCloseContextMenu();
  }
  provide('onShowDrawer', onShowDrawer);

  /**
   * 导出csv
   */
  function exportCsv() {
    tableRef.value?.downloadCsv({ fileName: props.datatable.title + '数据表' + new Date().getTime()});
  }
  function getCsvCell(value) {
    return value;
  }
  function getCsvHeader(column) {
    const exportColumn = props.datatable.columns.find(i => i.name == column.key);
    if (exportColumn) {
      return exportColumn.title;
    }
  }

  /**
   * 总结栏
   */
  function renderSummary(pageData: unknown): unknown | DataTableCreateSummary {
    const options = {};
    for (let column of summaryColumns.value) {
      options[column.name] = {
        value: h('span', {style: {color: 'var(--primary-color)'}}, (pageData as unknown as RowData[]).reduce(
            (prevValue, row) => prevValue + parseFloat(row[column.name]), 0)
        ),
        colSpan: 1
      }
    }
    return options;
  }
</script>

<style scoped lang="scss">
  :deep(.n-data-table-th__ellipsis) {
    max-width: 100% !important;
  }
  .datatable-action-wrapper {
    display: flex;
    margin-bottom: 10px;
    height: 34px;
  }
</style>