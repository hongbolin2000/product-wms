<template>
  <n-card>
    <div style="margin-bottom: 10px">
      <n-button type="primary">
        新增
      </n-button>
    </div>

    <n-data-table
        :columns="columns"
        :data="data"
        :pagination="pagination"
        size="small"
        :bordered="false"
    />
  </n-card>
</template>

<script setup lang="ts">
  /********************************************************************************
   * 浏览表格
   *
   * @author Berlin
   ********************************************************************************/

  const columns = [
    {
      title: '国家名称',
      key: 'countryName'
    },
    {
      title: '区域编号',
      key: 'areaCode'
    },
    {
      title: '区域名称',
      key: 'areaName'
    },
    {
      title: '联系人',
      key: 'contacts'
    },
    {
      title: '联系电话',
      key: 'phoneNo'
    },
    {
      title: '电子邮件',
      key: 'email'
    },
    {
      title: '操作人',
      key: 'operateBy'
    },
    {
      title: '操作时间',
      key: 'operateTime'
    }
  ];

  const data = [{
    countryName: '中国',
    areaCode: 'HB',
    areaName: '华北区',
    contacts: '张三',
    phoneNo: '13789183192',
    email: '1607859551@qq.com',
    operateBy: 'admin',
    operateTime: '2025-02-05 10:40'
  }]

</script>

<style scoped lang="scss">

</style>