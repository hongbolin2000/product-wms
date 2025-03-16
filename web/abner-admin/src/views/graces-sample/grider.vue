<template>
  <Graces.GridViewer module="ba01" name="storeGrider"/>
</template>

<script setup lang="ts" name="GridViewerSample">
  /********************************************************************************
   * 通用浏览表格示例界面
   *
   * @author Berlin
   ********************************************************************************/
  import {Graces} from '@/ploutos/index.ts';

  function ba01Cancel(data: string) {
    alert("aaa");
  }
  window['ba01Cancel'] = ba01Cancel;
</script>

<style scoped lang="scss">

</style>