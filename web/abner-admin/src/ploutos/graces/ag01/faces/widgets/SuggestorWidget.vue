<template>
  <n-auto-complete
      v-if="widget.mode == 'input'"
      v-model:value="widget.rowData[widget.name]"
      :options="options"
      :loading="loading"
      clearable
      @update:value="handleSearch"
      :show-empty="false"
      :get-show="handleGetShow"
  >
    <template
        #default="{ handleInput, handleBlur, handleFocus, value: slotValue }"
    >
      <n-input
          :value="slotValue"
          :placeholder="'输入' + widget.title"
          :disabled="widget.isDisabled"
          clearable
          :maxlength="widget.maxLength"
          :show-count="widget.maxLength > 0"
          @input="handleInput"
          @focus="handleFocus"
          @blur="handleBlur"
      />
    </template>
  </n-auto-complete>
  <n-select
      v-else-if="!limited"
      v-model:value="widget.rowData[widget.name]"
      filterable
      :placeholder="'选择' + widget.title"
      :options="options"
      :loading="loading"
      clearable
      remote
      @search="handleSearch"
      @clear="() => handleSearch('')"
      @update:value="handleUpdateValue"
      @focus="() => handleSearch('', props.widget.rowData[props.widget.name])"
      :disabled="widget.isDisabled"
  />
  <n-select
      v-else
      v-model:value="widget.rowData[widget.name]"
      :placeholder="'选择' + widget.title"
      :options="options"
      @update-value="handleUpdateValue"
      :disabled="widget.isDisabled"
  />
</template>

<script setup lang="ts">
  import {onBeforeUpdate, onMounted, type PropType, type Ref, shallowRef} from "vue";
  /********************************************************************************
   * 查询建议器
   *
   * @author Berlin
   ********************************************************************************/
  import type SuggestorWidgetProps from "@/ploutos/graces/ag01/faces/widgets/SuggestorWidgetProps.ts";
  import type ValueModel from "@/ploutos/graces/ag01/faces/ValueModel.ts";
  import {WidgetUtil} from "@/ploutos/graces/ag01/faces/widgets/WidgetUtil.ts";
  import {http} from '@/ploutos';
  import type {SelectOption} from "naive-ui";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    widget: {
      type: Object as PropType<SuggestorWidgetProps>,
      required: true
    }
  });

  /**
   * 下拉选项
   */
  const options: Ref<ValueModel[]> = shallowRef([]);

  /**
   * 限制查询最大数
   */
  const limited = shallowRef(false);

  /**
   * 是否查询中
   */
  const loading = shallowRef(false);

  /**
   * 组件加载前
   */
  onMounted(() => {
    // 缺省值
    let value = props.widget.rowData[props.widget.name];
    if (!value) {
      props.widget.rowData[props.widget.name] = null;
    }
    WidgetUtil.disabled(props.widget);

    // 加载时先查询(非修改界面)
    handleSearch('');
  });

  /**
   * 组件更新前
   */
  let initial = true;
  onBeforeUpdate(() => {
    const value = props.widget.rowData[props.widget.name];
    if (value == '') {
      props.widget.rowData[props.widget.name] = null;
    }

    // 更新时查询(修改界面)
    if (props.widget.rowData.id && !limited.value && initial && value) {
      initial = false;
      handleSearch('', props.widget.rowData[props.widget.name]);
    }
    WidgetUtil.disabled(props.widget);
  });

  /**
   * 查询
   */
  async function handleSearch(searchValue: string, fieldValue?: string) {
    try {
      loading.value = true;
      const params = {
        searchValue: searchValue, fieldValue: fieldValue,
      }

      // 动态输入参数
      const inputs = props.widget.input.split(",");
      for (let input of inputs) {
        params[input] = props.widget.rowData[input];
      }

      const response = await http.post("/ag01/suggestor/loadData", {
        module: props.widget.module,
        name: props.widget.suggestor,
        local: navigator.language,
        params: params
      });
      options.value = response.data.data;
      limited.value = response.data.maxRows == -1;
    } finally {
      loading.value = false
    }
  }

  /**
   * 选择
   */
  function handleUpdateValue(value: string, option: SelectOption) {
    if (option && props.widget.script) {
      const func = new Function( 'row', 'option', 'script', 'return eval(script)');
      func(props.widget.rowData, option, props.widget.script);
    }
    if (props.widget.onValueUpdate) {
      props.widget.onValueUpdate(props.widget.rowData[props.widget.name]);
    }
  }

  /**
   * 自动填充始终展示
   */
  function handleGetShow() {
    return true;
  }
</script>

<style scoped lang="scss">

</style>