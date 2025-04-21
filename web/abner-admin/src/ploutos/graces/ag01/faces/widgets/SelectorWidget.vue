<template>
  <span style="display: none">{{widget.rowData[widget.name]}}</span>
  <n-popover placement="bottom" trigger="click" :show-arrow="false" style="max-width: 50vw">
    <template #trigger>
      <n-select
          :show="false"
          :placeholder="'选择' + widget.title"
          v-model:value="widget.rowData[widget.name]"
          :options="options"
          clearable
      />
    </template>

    <n-spin :show="loading">
      <template #icon>
        <n-icon>
          <SettingsOutline />
        </n-icon>
      </template>

      <n-card :bordered="false">
        <DataTable
            :main-data-table="selector.datatable"
            :datatable="selector.datatable"
            :max-height="selector.datatable.maxHeight"
            @on-search="loadData"
            @on-sort="handelSort"
            :module="props.widget.module"
            :module-name="props.widget.selector"
            @on-double-click="handleDoubleClick"
        />
      </n-card>
    </n-spin>
  </n-popover>
</template>

<script setup lang="ts">
  import {onBeforeUpdate, onMounted, type PropType, provide, ref, type Ref, shallowRef, watch} from "vue";
  /********************************************************************************
   * 查询选择器
   *
   * @author Berlin
   ********************************************************************************/
  import type SelectorWidgetProps from "@/ploutos/graces/ag01/faces/widgets/SelectorWidgetProps.ts";
  import {http} from "@/ploutos";
  import type SelectorProps from "@/ploutos/graces/ag01/faces/widgets/SelectorProps.ts";
  import DataTable from "@/ploutos/graces/ag01/components/DataTable.vue";
  import {WidgetUtil} from "@/ploutos/graces/ag01/faces/widgets/WidgetUtil.ts";
  import {NIcon} from "naive-ui";
  import {SettingsOutline} from "@vicons/ionicons5";
  import type ValueModel from "@/ploutos/graces/ag01/faces/ValueModel.ts";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    widget: {
      type: Object as PropType<SelectorWidgetProps>,
      required: true
    }
  });

  /**
   * 查询选择器界面属性定义
   */
  const selector: Ref<SelectorProps> = ref();

  /**
   * 查询条件
   */
  const params: Ref = ref({});

  /**
   * 是否查询中
   */
  const loading = shallowRef(false);

  /**
   * 排序字段
   */
  const sorter: Ref = ref(null);

  /**
   * 选择的选项
   */
  const options: Ref<ValueModel[]> = shallowRef([]);

  /**
   * 组件加载
   */
  onMounted( async () => {
    // 缺省值
    let value = props.widget.rowData[props.widget.name];
    if (!value) {
      props.widget.rowData[props.widget.name] = null;
    }
    WidgetUtil.disabled(props.widget);

    // 加载界面
    await loadDefine();
    await loadData();

    // 监听选择
    watchCheckedKey();
  });

  /**
   * 组件更新前
   */
  onBeforeUpdate(() => {
    // 初始化显示值(修改界面)
    let value = props.widget.rowData[props.widget.name];
    if (value && options.value.length <= 0) {
      options.value = [{
        label: props.widget.rowData[props.widget.labelColumn],
        value: value
      }];
    }
    WidgetUtil.disabled(props.widget);
  });

  /**
   * 加载界面定义
   */
  async function loadDefine() {
    const response = await http.post("/ag01/selector/loadDefine", {
      module: props.widget.module,
      name: props.widget.selector,
      local: navigator.language,
      params: params.value
    });
    selector.value = response.data;
  }

  /**
   * 加载界面数据
   */
  async function loadData() {
    try {
      loading.value = true;
      const response = await http.post("/ag01/selector/loadData", {
        module: props.widget.module,
        name: props.widget.selector,
        local: navigator.language,
        params: params.value,
        sorter: sorter.value
      });
      selector.value.datatable.data = response.data.data;
    } finally {
      loading.value = false;
    }
  }
  provide('params', params.value);

  /**
   * 监听选择
   */
  function watchCheckedKey() {
    watch(() => selector.value.datatable.checkedKeys, () => {
      // 获取选择的key
      const datatable = selector.value.datatable;
      const checkedKeys = datatable.checkedKeys;
      if (!checkedKeys || checkedKeys.length <= 0) {
        return;
      }

      // 生成选项
      props.widget.rowData[props.widget.name] = checkedKeys[0];
      const option = datatable.data.find(i => i[datatable.checkRowKey] == checkedKeys[0]);
      options.value = [{
        label: option[datatable.labelColumn],
        value: checkedKeys[0]
      }];

      // 脚本
      if (props.widget.script) {
        const func = new Function( 'row', 'option', 'script', 'return eval(script)');
        func(props.widget.rowData, option, props.widget.script);
      }
    });
  }

  /**
   * 表格排序
   */
  function handelSort(state: any) {
    sorter.value = state;
    loadData();
  }

  /**
   * 表格双击
   */
  function handleDoubleClick(rowData: any) {
    selector.value.datatable.checkedKeys = [rowData[selector.value.datatable.checkRowKey]];
  }
</script>

<style scoped lang="scss">

</style>