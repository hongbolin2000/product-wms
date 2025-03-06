<template>
  <n-popover placement="bottom" trigger="hover">
    <template #trigger>
      <span :class="{'title-active' : value[column?.name]}">
        {{column.title}}
      </span>
    </template>

    <n-space :size="10">
      <n-input
          type="text"
          :placeholder="column?.title!"
          v-model:value="value[column?.name]"
          style="width: 150px"
          clearable
      />
      <n-button @click="handleQuery">查询</n-button>
    </n-space>
  </n-popover>
</template>

<script setup lang="ts">
  /********************************************************************************
   * 表格列标题
   *
   * @author Berlin
   ********************************************************************************/
  import {inject, type PropType, type Ref, ref} from "vue";
  import type AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";

  /**
   * 注入查询参数
   */
  const value: Ref = ref(inject('params'));

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    column: Object as PropType<AbstractColumn>
  });

  /**
   * 注入查询函数
   */
  const onSearch = inject('onSearch');

  /**
   * 查询
   */
  function handleQuery() {
    onSearch();
  }
</script>

<style scoped lang="scss">
  .title-active {
    color: var(--primary-color)
  }
</style>