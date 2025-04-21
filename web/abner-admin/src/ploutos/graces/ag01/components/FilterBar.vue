<template>
  <n-form :label-placement="'left'" label-width="auto">
    <n-grid :cols="1">
      <n-form-item-gi v-for="column of filterColumns" :span="1" :label="column.title" :key="column.name">
        <n-input-group>
          <n-input
              type="text"
              :placeholder="column.title"
              v-model:value="params[column.name]"
              clearable
              v-if="column?.filter == 'text'"
          />
          <n-date-picker
              v-model:value="params[column.name]"
              type="datetimerange"
              clearable
              v-if="column.filter == 'date'"
              update-value-on-close
          />
          <n-select
              :options="column.filterOptions"
              :clearable="true"
              :placeholder="column.title"
              v-model:value="params[column?.name]"
              v-if="column.filterOptions"
              :onUpdate:value="onSearch"
          ></n-select>

          <n-button @click="onSearch">
            查询
          </n-button>
          <n-button @click="handleClear(column)">
            重置
          </n-button>
        </n-input-group>
      </n-form-item-gi>
    </n-grid>
  </n-form>
</template>

<script setup lang="ts">
import {inject, onMounted, type PropType, type Ref, ref} from "vue";
  /********************************************************************************
   * 过滤栏
   *
   * @author Berlin
   ********************************************************************************/
  import type AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";

  /**
   * 父组件传入属性
   */
  const props = defineProps({
    columns: {
      type: Object as PropType<AbstractColumn[]>,
      required: true
    }
  });

  /**
   * 注入查询参数
   */
  const params: Ref = ref(inject('params'));

  /**
   * 注入查询函数
   */
  const onSearch = inject<Function>('onSearch');

  /**
   * 可过滤的列
   */
  const filterColumns: Ref<AbstractColumn[]> = ref([]);

  /**
   * 组件加载
   */
  onMounted(() => {
    filterColumns.value = props.columns.filter(i => i.filter);
  });

  /**
   * 清除
   */
  function handleClear(column: AbstractColumn) {
    params.value[column.name] = null;
    onSearch();
  }
</script>

<style scoped lang="scss">

</style>