<template>
  <n-space vertical :size="10">
    <div>{{title}}</div>

    <n-scrollbar style="max-height: 60vh">
      <n-grid :cols="4">
        <n-gi v-for="(text, index) of content" :key="index">
          {{text}}
          <n-divider style="margin-top: 3px;margin-bottom: 10px"/>
        </n-gi>
      </n-grid>
    </n-scrollbar>
  </n-space>
</template>

<script setup lang="ts">
  /********************************************************************************
   * Dialog批量数据提示
   *
   * @author Berlin
   ********************************************************************************/

  /**
   * 父组件传入属性
   */
  defineProps({
    title: {
      type: String,
      required: true
    },
    content: {
      type: Array<string>,
      required: true
    }
  });
</script>

<style scoped lang="scss">

</style>