<template>
  <div>
    <!--  <Graces.GridViewer module="sy99" name="storeGrider" @onSearch="onSearch" :query="false" @onSubSearch="onSearch" :query-sub="false"/>-->
    <Graces.GridViewer module="sy99" name="storeGrider" @onSearch="onSearch"/>
  </div>
</template>

<script setup lang="ts" name="GridViewerSample">
  /********************************************************************************
   * 通用浏览表格示例界面
   *
   * @author Berlin
   ********************************************************************************/
  import {Graces} from '@/ploutos';

  function sy99Cancel(data: string) {
    alert("aaa");
  }
  window['sy99Cancel'] = sy99Cancel;

  function onSearch(params: object, sorter: Graces.Sorter, pageNumber?: number, pageSize?: number) {
    debugger;
  }
</script>

<style scoped lang="scss">

</style>