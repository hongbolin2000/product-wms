<template>
  <transition name="opacity-transform">
    <div v-if="grider.datatable">
      <n-card>
        <n-space :size="10" style="margin-bottom: 10px;">
          <component
              v-for="action of grider.actions"
              :is="ActionFactories.getInstance().create(action)"
          />
        </n-space>

        <DataTable
            :datatable="grider.datatable"
            :max-height="maxHeight"
            need-pagination
            @on-sort="handelSort"
            @on-pagination="handelPagination"
            @on-search="handelSearch"
            @on-double-click="handelDoubleClick"
        />
      </n-card>

      <div v-if="grider.subTables" class="sub-table">
        <n-card
            v-for="datatable of grider.subTables"
            :style="{width: getFormWidth(datatable)}"
        >
          <DataTable
              :datatable="datatable"
              :max-height="maxHeight"
              @on-double-click="handelDoubleClick"
              @on-search="loadSubTablesData"
              @on-sort="handelSubSort"
          />
        </n-card>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
  import {computed, onMounted, provide, ref, type Ref} from "vue";
  /********************************************************************************
   * 通用表格浏览界面
   *
   * @author Berlin
   ********************************************************************************/
  import {http, loading} from "@/ploutos";
  import ActionFactories from "@/ploutos/graces/ag01/faces/ActionFactories.ts";
  import type GriderProps from "@/ploutos/graces/ag01/grid-viewer/GriderProps.ts";
  import DataTable from "@/ploutos/graces/ag01/components/DataTable.vue";
  import type DoubleClick from "@/ploutos/graces/ag01/faces/DoubleClick.ts";
  import type Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";

  /**
   * 浏览表格属性定义
   */
  const grider: Ref<GriderProps> = ref({});

  /**
   * 过滤条件
   */
  const params: Ref = ref({});

  /**
   * 主表排序字段
   */
  const sorter: Ref = ref(null);

  /**
   * 子表排序字段
   */
  const subSorter: Ref = ref(null);

  /**
   * 分页属性
   */
  const pagination = ref({
    pageNumber: 1,
    pageSize: 20
  })

  /**
   * 父组件传入的属性
   */
  const props = defineProps({

    /**
     * 模块号
     */
    module: {
      type: String,
      required: true
    },

    /**
     * 界面名称
     */
    name: {
      type: String,
      required: true
    },

    /**
     * 参数
     */
    params: {
      type: Object
    },
  });

  /**
   * 计算表格最大高度
   */
  const maxHeight = computed(() => {
    if (!grider.value.subTables) {
      return 'calc(100vh - var(--logo-height) - var(--tab-height) - 180px)';
    }
    return 'calc((100vh - var(--logo-height) - var(--tab-height) - 270px) / 2)';
  });

  /**
   * 组件加载
   */
  onMounted(() => {
    loadDefine();
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
      loading(true);
      clearSubTablesData();

      const response = await http.post("/ag01/grider/loadTableData", {
        module: props.module,
        name: props.name,
        local: navigator.language,
        sorter: sorter.value,
        params: params.value,
        pageNumber: pagination.value.pageNumber,
        pageSize: pagination.value.pageSize
      });
      grider.value.datatable.data = response.data.data;

      // 第一页才查询总记录数
      if (pagination.value.pageNumber == 1) {
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
    pagination.value.pageNumber = 1;
    sorter.value = state;
    loadTableData();
  }

  /**
   * 条件搜索
   */
  function handelSearch() {
    pagination.value.pageNumber = 1;
    loadTableData();
  }
  provide('params', params.value);

  /**
   * 分页
   */
  function handelPagination(option: any) {
    pagination.value = option;
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
      if (!doubleClick) {
        return;
      }

      loading(true);

      const data = {
        module: props.module, name: props.name, local: 'zh-CN', params: params.value,
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