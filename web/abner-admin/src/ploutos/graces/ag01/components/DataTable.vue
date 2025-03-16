<template>
  <div class="datatable-action-wrapper" v-if="showTools">
    <n-space :size="10">
      <component v-for="action of actions" :is="() => {
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
      :scroll-x="0"
      remote
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
    type VNode,
    onMounted
  } from "vue";
  import {type DataTableSortState, type DropdownOption, NIcon} from "naive-ui";
  import {ChevronDown, Expand, RefreshOutline} from '@vicons/ionicons5'
  /********************************************************************************
   * 数据表格
   *
   * @author Berlin
   ********************************************************************************/
  import type Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";
  import type {TableBaseColumn, TableSelectionColumn} from "naive-ui/es/data-table/src/interface";
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

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    mainDataTable: {
      type: Object as PropType<Datatable>,
      required: true
    },
    datatable: {
      type: Object as PropType<Datatable>,
      required: true
    },
    needPagination: {
      type: Boolean,
    },
    maxHeight: {
      type: String
    },
    showTools: {
      type: Boolean,
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
   * 右键link执行传出的值
   */
  const componentValue: Ref = ref(undefined);

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
  onMounted(() => {
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
      item.datatableTitle = datatable.title;
      item.rowData = selectRowData;

      // 操作按钮
      if (ColumnFactories.isLink(item)) {
        menuOptions.push({
          key: item.name,
          type: 'render',
          render: () => {
            return ColumnFactories.getInstance().create({...item});
          },
        });
        return;
      }

      // 选择列
      if (item.type == CheckColumnFactory.TYPE) {
        const checkColumn: CheckColumnProps = <CheckColumnProps>{...item};

        const column: TableSelectionColumn = <TableSelectionColumn>{};
        column.type = 'selection';
        column.disabled = (row: object) => {
          if (checkColumn.disabled) {
            const func = new Function( 'rowData', 'disabled', 'return eval("rowData." + disabled)');
            return func(row, checkColumn.disabled);
          }
        }
        column.multiple = !checkColumn.single;
        datatable.checkRowKey = checkColumn.name;
        // @ts-ignore
        columns.push(column);
        return;
      }

      const column: TableBaseColumn = <TableBaseColumn>{};
      column.key = item.name;
      column.sorter = true;
      column.width = item.width ? item.width : 100;
      column.resizable = TagColumnFactory.TYPE != item.type;
      column.ellipsis = TagColumnFactory.TYPE != item.type;

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