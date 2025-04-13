<template>
  <transition name="opacity-transform">
    <div v-if="grider && grider.datatable">
      <n-card>
        <DataTable
            :static="static"
            :datatable="grider.datatable"
            :max-height="maxHeight"
            need-pagination
            :show-tools="showTools != undefined ? showTools : true"
            @on-sort="handelSort"
            @on-pagination="handelPagination"
            @on-search="handelSearch"
            @on-double-click="handelDoubleClick"
            :module="props.module"
            :module-name="props.name"
        />
      </n-card>

      <div v-if="grider.subTables" class="sub-table">
        <n-card
            v-for="datatable of grider.subTables"
            :style="{width: getFormWidth(datatable)}"
            :key="datatable.name"
        >
          <DataTable
              :static="static"
              :main-data-table="grider.datatable"
              :datatable="datatable"
              :max-height="maxHeight"
              @on-double-click="handelDoubleClick"
              @on-search="loadSubTablesData"
              @on-sort="handelSubSort"
              :module="props.module"
              :module-name="props.name"
          />
        </n-card>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
  import {onMounted, provide, ref, type Ref, watch} from "vue";
  import {storeToRefs} from "pinia";
  import {useRoute} from "vue-router";
  /********************************************************************************
   * 通用表格浏览界面
   *
   * @author Berlin
   ********************************************************************************/
  import {http, loading} from "@/ploutos";
  import type GriderProps from "@/ploutos/graces/ag01/grid-viewer/GriderProps.ts";
  import DataTable from "@/ploutos/graces/ag01/components/DataTable.vue";
  import type DoubleClick from "@/ploutos/graces/ag01/faces/DoubleClick.ts";
  import type Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store";
  import useAppStore from "@/ploutos/layouts/store/app-store.ts";

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();
  const appStore = useAppStore();
  const route = useRoute();

  /**
   * 浏览表格属性定义
   */
  const grider: Ref<GriderProps> = ref();

  /**
   * 主表排序字段
   */
  const sorter: Ref = ref(null);

  /**
   * 子表排序字段
   */
  const subSorter: Ref = ref(null);

  /**
   * 分页器
   */
  const pager = ref({
    pageNumber: 1,
    pageSize: 20
  })

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    module: {
      type: String,
      required: true
    },
    name: {
      type: String,
      required: true
    },
    params: {
      type: Object
    },
    // 是否显示工具栏
    showTools: {
      type: Boolean,
      default: undefined
    },
    // 是否静态界面，只加载界面定义，数据由功能设置
    static: {
      type: Boolean
    },
    // 是否自定义主表查询函数
    query: {
      type: Boolean,
      default: true
    },
    // 是否自定义子表查询函数
    querySub: {
      type: Boolean,
      default: true
    }
  });

  /**
   * 父组件事件
   */
  const emit = defineEmits<{

    /**
     * 自定义主表查询
     */
    (e: 'onSearch', params: object, sorter: {name: string | number, order: 'asc' | 'desc'},
     pageNumber: number, pageSize: number
    ): void;

    /**
     * 自定义子表查询
     */
    (e: 'onSubSearch', params: object, sorter: {name: string | number, order: 'asc' | 'desc'}): void;
  }>();

  /**
   * 过滤条件
   */
  const params: Ref = ref(props.params ? props.params : new Object({}));

  /**
   * 表格最大高度
   */
  const maxHeight = ref('');

  /**
   * 计算表格最大高度
   */
  function getMaxHeight() {
    if (!grider.value) {
      return;
    }

    let expr = '';
    if (!layoutStore.isFullScreen) {
      // 减去顶部高度
      expr += '- var(--logo-height) - var(--tab-height)';
    } else {
      // 减去全屏时的margin高度
      expr += ' - 10px';
    }
    // 加上工具栏高度
    if (props.showTools != undefined && !props.showTools) {
      expr += ' + 44px'
    }
    // 加上分页器高度
    if (props.static) {
      expr += ' + 40px'
    }

    // 计算最终高度
    if (!grider.value.subTables) {
      maxHeight.value = 'calc(100vh ' + expr + ' - 180px)';
    } else {
      maxHeight.value = 'calc((100vh ' + expr + ' - 270px) / 2)';
    }
  }

  /**
   * 监听布局状态变化
   */
  const {isFullScreen} = storeToRefs(layoutStore);
  watch(isFullScreen, () => {
    getMaxHeight();
  });

  /**
   * 组件加载
   */
  onMounted(async () => {

    // 将刷新事件写入window
    const name = props.module + props.name.substring(0, 1).toUpperCase() + props.name.substring(1) + "Refresh";
    window[name] = loadTableData;

    setTimeout(async() => {
      await loadDefine();
      getMaxHeight();
    }, 300);
  });

  /**
   * 加载界面定义
   */
  async function loadDefine() {
    try {
      loading(true);

      const response = await http.post("/ag01/grider/loadDefine", {
        module: props.module,
        name: props.name,
        local: navigator.language,
        params: params
      });
      grider.value = response.data;

      await loadTableData();

      // 其他列表跳转的界面
      if (route.query.from) {
        const datatable = grider.value.datatable;
        let tabTitle = datatable.title;
        if (datatable.data.length > 0) {
          tabTitle += ' - ' + datatable.data[0][datatable.labelColumn]
        }
        appStore.changeTabTitle(tabTitle);
      }
    } finally {
      loading(false);
    }
  }

  /********************************************************************************
   * 主表查询函数 Start
   ********************************************************************************/

  /**
   * 加载主表数据
   */
  async function loadTableData() {
    try {
      if (props.static) {
        return;
      }
      if (!props.query) {
        emit('onSearch', params.value, sorter.value, pager.value.pageNumber, pager.value.pageSize);
        return;
      }

      loading(true);
      clearSubTablesData();

      const response = await http.post("/ag01/grider/loadTableData", {
        module: props.module,
        name: props.name,
        local: navigator.language,
        sorter: sorter.value,
        params: params.value,
        pageNumber: pager.value.pageNumber,
        pageSize: pager.value.pageSize
      });
      grider.value.datatable.data = response.data.data;

      // 第一页才查询总记录数
      if (response.data.total) {
        grider.value.datatable.total = response.data.total;
      }
    } finally {
      loading(false);
    }
  }

  /**
   * 排序
   */
  function handelSort(state: any) {
    pager.value.pageNumber = 1;
    sorter.value = state;
    loadTableData();
  }

  /**
   * 条件搜索
   */
  function handelSearch() {
    pager.value.pageNumber = 1;
    loadTableData();
  }
  provide('params', params.value);

  /**
   * 分页
   */
  function handelPagination(pagination: any) {
    pager.value = pagination;
    loadTableData();
  }

  /**
   * 表格双击执行事件
   */
  let doubleClick: DoubleClick = undefined!;
  function handelDoubleClick(rowData: any) {
    doubleClick = grider.value.datatable.doubleClick;
    params.value[doubleClick.input] = rowData[doubleClick.input];
    loadSubTablesData();
  }

  /********************************************************************************
   * 子表查询函数 Start
   ********************************************************************************/

  /**
   * 查询子表数据
   */
  async function loadSubTablesData() {
    try {
      if (!doubleClick || props.static) {
        return;
      }
      if (!props.querySub) {
        emit('onSubSearch', params.value, sorter.value);
        return;
      }

      loading(true);

      const data = {
        module: props.module, name: props.name, local: navigator.language, params: params.value,
        datatables: grider.value.datatable.doubleClick.datatables,
        sorter: subSorter.value
      }
      const response = await http.post("/ag01/grider/loadSubTablesData", data);

      // 子表数据
      grider.value.subTables.forEach(datatable => {
        if (response.data[datatable.name]) {
          datatable.data = response.data[datatable.name];
        }
      });
    } finally {
      loading(false);
    }
  }

  /**
   * 子表排序
   */
  function handelSubSort(state: any) {
    subSorter.value = state;
    loadSubTablesData();
  }

  /**
   * 清楚子表数据
   */
  function clearSubTablesData() {
    if (!grider.value.subTables || !doubleClick) {
      return;
    }
    grider.value.subTables.forEach(table => {
      table.data = [];
    });
    delete params.value[doubleClick.input];
    doubleClick = undefined!;
  }

  /**
   * 表格的宽度
   */
  function getFormWidth(table: Datatable) {
    // 表格与表格之间的间隙
    const subTables = grider.value.subTables;
    const gap = 10 * (subTables.length - 1) / subTables.length;

    // 表格设置了宽度
    if (table.width) {
      return 'calc(' + table.width + ' - ' + gap + 'px)';
    }

    // 未设置宽度的表格
    let restWidth = "100% ", restCount = 0;
    subTables.forEach(datatable => {
      if (datatable.width) {
        restWidth += "- " + datatable.width;
      } else {
        restCount++;
      }
    });
    // 未设置宽度的表格宽度
    return 'calc((' + restWidth + ') / ' + restCount + ' - ' + gap + 'px)';
  }
</script>

<style scoped lang="scss">
  .sub-table {
    display: flex;
    gap: 10px;
    margin-top: 10px
  }

  /* 渐隐渐现页面动画效果 */
  .opacity-transform-leave-active,
  .opacity-transform-enter-active {
    transition: all 0.5s;
  }
  .opacity-transform-enter-from {
    opacity: 0;
  }
  .opacity-transform-leave-to {
    opacity: 0;
  }
</style>