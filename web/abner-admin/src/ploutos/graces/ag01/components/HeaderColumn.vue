<template>
  <n-popover placement="bottom" trigger="hover" v-if="showPopover">
    <template #trigger>
      <span :class="{'title-active' : value[column?.name]}">
        {{column.title}}
      </span>
    </template>

    <n-space :size="10">
      <n-input-group>
        <n-input
            type="text"
            :placeholder="column?.title!"
            v-model:value="value[column?.name]"
            style="width: 150px"
            clearable
            v-if="column?.filter == 'text'"
        />
        <n-date-picker
            v-model:value="value[column?.name]"
            type="datetimerange"
            clearable
            v-if="column?.filter == 'date'"
            update-value-on-close
        />

        <n-button @click="handleQuery">
          查询
        </n-button>
        <n-button @click="handleClear">
          清除
        </n-button>
      </n-input-group>
    </n-space>
  </n-popover>

  <n-popselect
      v-model:value="value[column?.name]"
      :options="column?.filterOptions!"
      trigger="hover"
      v-if="column?.filterOptions"
      :onUpdate:value="handleQuery"
  >
    <span :class="{'title-active' : value[column?.name]}">
      {{column.title}}
    </span>
  </n-popselect>
</template>

<script setup lang="ts">
  /********************************************************************************
   * 表格列标题
   *
   * @author Berlin
   ********************************************************************************/
  import {computed, inject, onMounted, type PropType, type Ref, ref} from "vue";
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
  const onSearch = inject<Function>('onSearch');

  /**
   * 组件加载
   */
  onMounted(() => {
    if (props.column?.filterOptions) {
      props.column.filterOptions.unshift({
        label: '全部',
        value: ''
      })
    }
  });

  /**
   * 是否使用弹出框
   */
  const showPopover = computed(() => {
    const filter = props.column?.filter;
    return filter == 'text' || filter == 'date';
  });

  /**
   * 查询
   */
  function handleQuery() {
    onSearch();
  }

  /**
   * 清除
   */
  function handleClear() {
    value.value[props.column.name] = null;
    onSearch();
  }
</script>

<style scoped lang="scss">
  .title-active {
    color: var(--primary-color)
  }
</style>