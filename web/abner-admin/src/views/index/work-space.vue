<template>
  <n-space vertical :size="12">
    <n-space>
      <n-button @click="sortName">Sort By Name (Ascend)</n-button>
      <n-button @click="filterAddress">Filter Address (London)</n-button>
      <n-button @click="clearFilters">Clear Filters</n-button>
      <n-button @click="clearSorter">Clear Sorter</n-button>
    </n-space>
    <n-data-table
        :columns="columns"
        :data="data"
        :pagination="pagination"
    />
  </n-space>
</template>

<script>
import { defineComponent, ref } from 'vue'

const columns = [
  {
    title: 'Name',
    key: 'name',
    defaultSortOrder: 'ascend',
    sorter: 'default'
  }
]

const data = [
  {
    key: 0,
    name: 'John Brown',
    age: 32,
    address: 'New York No. 1 Lake Park'
  },{
    key: 0,
    name: 'Bohn Brown',
    age: 32,
    address: 'New York No. 1 Lake Park'
  },{
    key: 0,
    name: 'Cohn Brown',
    age: 32,
    address: 'New York No. 1 Lake Park'
  },{
    key: 0,
    name: 'Dohn Brown',
    age: 32,
    address: 'New York No. 1 Lake Park'
  }
]

export default defineComponent({
  setup() {
    return {
      data,
      columns,
      pagination: { pageSize: 5 },
    }
  }
})
</script>