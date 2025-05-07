<template>
  <n-space size="small">
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
        <n-button secondary size="small" @click="emit('onExportExcel')" class="tool-item">
          <SvgIcon name="csv"/>
        </n-button>
      </template>
      导出Excel
    </n-tooltip>
  </n-space>
</template>

<script setup lang="ts">
  import {NIcon} from "naive-ui";
  import {Expand, RefreshOutline} from "@vicons/ionicons5";
  /********************************************************************************
   * 表格头部操作工具栏
   *
   * @author Berlin
   ********************************************************************************/
  import SvgIcon from "@/ploutos/layouts/icons/SvgIcon.vue";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store.ts";
  import {inject, type PropType} from "vue";
  import type Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";

  /**
   * 父组件传入属性
   */
  defineProps({
    // 主数据表
    mainDataTable: {
      type: Object as PropType<Datatable>,
      required: true
    },
  });

  /**
   * 父组件事件
   */
  const emit = defineEmits<{

    /**
     * 导出Excel
     */
    (e: 'onExportExcel'): void;
  }>();

  /**
   * 注入查询函数
   */
  const onSearch = inject<Function>('onSearch');

  /**
   * 布局状态
   */
  const layoutStore = useLayoutStore();
</script>

<style scoped lang="scss">
  .tool-item {
    &:hover {
      cursor: pointer;
      color: var(--primary-color-hover);
    }
  }
</style>