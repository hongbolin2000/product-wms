<template>
  <div>
    <!--  <Graces.GridViewer module="ba01" name="storeGrider" @onSearch="onSearch" :query="false" @onSubSearch="onSearch" :query-sub="false"/>-->
    <Graces.GridViewer module="ba01" name="storeGrider" @onSearch="onSearch"/>
  </div>
</template>

<script setup lang="ts" name="GridViewerSample">
  /********************************************************************************
   * 通用浏览表格示例界面
   *
   * @author Berlin
   ********************************************************************************/
  import {Graces} from '@/ploutos';

  function ba01Cancel(data: string) {
    alert("aaa");
  }
  window['ba01Cancel'] = ba01Cancel;

  function onSearch(params: object, sorter: Graces.Sorter, pageNumber?: number, pageSize?: number) {
    debugger;
  }
</script>

<style scoped lang="scss">

</style>