<template>
  <n-space :size="0">
    <n-button type="primary" text @click="onUpdateClick" v-if="sheeter.updated">修改</n-button>
    <n-divider vertical v-if="sheeter.added && sheeter.updated"/>

    <n-popconfirm @positive-click="onDeleteClick" v-if="sheeter.added">
      <template #trigger>
        <n-button type="error" text>删除</n-button>
      </template>
      是否确认删除当前行数据？
    </n-popconfirm>
  </n-space>
</template>

<script setup lang="ts">
  import type {PropType} from "vue";
  /********************************************************************************
   * @author Berlin
   ********************************************************************************/
  import type Sheeter from "@/ploutos/graces/ag01/faces/Sheeter.ts";

  /**
   * 父组件传入属性
   */
  defineProps({
    sheeter: {
      type: Object as PropType<Sheeter>,
      required: true,
    },
    onUpdateClick: {
      type: Function,
      required: true
    },
    onDeleteClick: {
      type: Function,
      required: true
    }
  })
</script>

<style scoped lang="scss">

</style>