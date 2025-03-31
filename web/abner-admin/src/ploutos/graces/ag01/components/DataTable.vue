<template>
  <div class="datatable-action-wrapper" v-if="showTools">
    <n-space :size="10">
      <component v-for="action of actions" :key="action.name" :is="() => {
        action.datatable = datatable;
        return ActionFactories.getInstance().create(action)
      }"/>

      <n-dropdown trigger="hover" :options="optionActions" v-if="optionActions.length > 0">
        <n-button icon-placement="right" class="more-action">
          <template #icon>
            <n-icon class="tip">
              <ChevronDown style="font-size: 14px"/>
            </n-icon>
          </template>
          更多操作
        </n-button>
      </n-dropdown>
    </n-space>

    <div style="flex: 1" v-if="datatable.actions.length > 0"></div>
    <n-space size="small" :style="datatable.actions.length <= 0 ? 'flex-direction: row-reverse' : ''">
      <n-tooltip trigger="hover" v-if="!layoutStore.bordered">
        <template #trigger>
          <n-switch v-model:value="mainDataTable.bordered" size="small"/>
        </template>
        边框
      </n-tooltip>

      <n-tooltip trigger="hover" v-if="!layoutStore.striped">
        <template #trigger>
          <n-switch v-model:value="mainDataTable.striped" size="small"/>
        </template>
        斑马格
      </n-tooltip>

      <n-tooltip trigger="hover">
        <template #trigger>
          <n-button secondary size="small" @click="onSearch()" class="tool-item">
            <n-icon size="18">
              <RefreshOutline/>
            </n-icon>
          </n-button>
        </template>
        刷新
      </n-tooltip>

      <n-tooltip trigger="hover">
        <template #trigger>
          <n-button secondary size="small" @click="layoutStore.fullScreen()" class="tool-item">
            <n-icon size="18">
              <Expand/>
            </n-icon>
          </n-button>
        </template>
        全屏
      </n-tooltip>

      <n-tooltip trigger="hover">
        <template #trigger>
          <n-button secondary size="small" @click="exportCsv()" class="tool-item">
            <SvgIcon name="csv"/>
          </n-button>
        </template>
        导出csv 导出后请使用记事本另存为同名文件编码选择ASNI后再打开
      </n-tooltip>
    </n-space>
  </div>

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
  />
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
      :mask-closable="false"
  >
    <component :is="component" :params="componentParams" :is-dialog="true"
               @on-close="showModal = false" @on-refresh="onSearch"
    />
  </n-modal>

  <n-drawer v-model:show="showDrawer" :width="400" placement="right" :mask-closable="false">
    <n-drawer-content :title="selectColumn.title" closable :body-content-style="{padding: 0}">
      <component :is="component" :params="componentParams" :is-drawer="true"
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
    import {type DataTableSortState, type DropdownOption, NIcon} from "naive-ui";
    import {ChevronDown, Expand, RefreshOutline} from '@vicons/ionicons5'
  import {Parser} from "expr-eval";
  /********************************************************************************
   * 数据表格
   *
   * @author Berlin
   ********************************************************************************/
  import type Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";
  import type {TableBaseColumn, TableColumn, TableSelectionColumn} from "naive-ui/es/data-table/src/interface";
  import ColumnFactories from "@/ploutos/graces/ag01/faces/ColumnFactories.ts";
  import type AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";
  import HeaderColumn from "@/ploutos/graces/ag01/components/HeaderColumn.vue";
  import LabelColumnFactory from "@/ploutos/graces/ag01/faces/columns/LabelColumnFactory.ts";
  import type LinkColumnProps from "@/ploutos/graces/ag01/faces/columns/LinkColumnProps.ts";
  import CheckColumnFactory from "@/ploutos/graces/ag01/faces/columns/CheckColumnFactory.ts";
  import type CheckColumnProps from "@/ploutos/graces/ag01/faces/columns/CheckColumnProps.ts";
  import TagColumnFactory from "@/ploutos/graces/ag01/faces/columns/TagColumnFactory.ts";
  import ActionFactories from "@/ploutos/graces/ag01/faces/ActionFactories.ts";
  import type AbstractAction from "@/ploutos/graces/ag01/faces/AbstractAction.ts";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store.ts";
  import SvgIcon from "@/ploutos/layouts/icons/SvgIcon.vue";
  import ColumnActions from "@/ploutos/graces/ag01/components/ColumnActions.vue";

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
   * 展示按钮
   */
  const actions: Ref<AbstractAction[]> = shallowRef([]);
  const optionActions: Ref<DropdownOption[]> = shallowRef([]);

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

    if (props.datatable.actions) {
      actions.value = props.datatable.actions.filter(i => !i.option);
      const options = props.datatable.actions.filter(i => i.option);

      const actionOptions: DropdownOption[] = []
      options.forEach(action => {
        actionOptions.push({
          key: action.name,
          type: 'render',
          render: () => {
            action.datatable = props.datatable;
            return ActionFactories.getInstance().create(action);
          },
        });
      });
      optionActions.value = actionOptions;
    }
  });

  /**
   * 表格总宽度
   */
  const tableWidth = computed(() => {
    let width = 0;
    props.datatable.columns.forEach(column => {
      width += column.width ? parseInt(column.width.toString()) : 100;
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
  function renderColumns(datatable: Datatable): TableColumn[] {
    const columns: TableColumn[] = [];
    const menuOptions = [];

    // 右键操作按钮（所有按钮列都能在右键中使用）
    datatable.columns.filter(i => ColumnFactories.isLink(i)).forEach(column => {
      column.rowData = selectRowData;
      column.datatableTitle = datatable.title;
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
    datatable.columns.filter(i => i.type == CheckColumnFactory.TYPE).forEach(item => {
      const checkColumn: CheckColumnProps = <CheckColumnProps>{...item};

      const column: TableSelectionColumn = <TableSelectionColumn>{};
      column.type = 'selection';
      column.disabled = (row: any) => {
        if (checkColumn.disabled) {
          return Parser.parse(checkColumn.disabled).evaluate(row);
        }
      }
      column.multiple = !checkColumn.single;
      datatable.checkRowKey = checkColumn.name;
      column.cellProps = (rowData: object, rowIndex: number) => columnProps(rowIndex);
      columns.push(column);
    });

    // 生成表格列
    datatable.columns.forEach(item => {
      if (ColumnFactories.isLink(item) || item.type == CheckColumnFactory.TYPE) {
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
    const defaultWidth = menuOptions.length > 0 ? 25 : 0;
    const column: TableBaseColumn = <TableBaseColumn>{width: 0};
    const columnActions = datatable.columns.filter(i => ColumnFactories.isLink(i) && !i.option);
    if (columnActions.length > 0) {
      columnActions.forEach(item => {
        const width = item.width ? parseInt(item.width.toString()): 80 + defaultWidth;
        column.width = parseInt(column.width.toString()) + width;
      });
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
      params: {value: string, module: string, name: string}
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
    return props.datatable.columns.find(i => i.name == column.key).title;
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

    .more-action {
      .tip {
        transition: transform $transitionTime;
      }
      &:hover {
        .tip {
          transform: rotate(180deg);
        }
      }
    }
    .tool-item {
      &:hover {
        cursor: pointer;
        color: var(--primary-color-hover);
      }
    }
  }
</style>