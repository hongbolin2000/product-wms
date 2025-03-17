<template>
  <div>
    <n-date-picker v-model:value="timestamp" type="week" />
    <pre>{{ moment(timestamp).format("YYYY-MM-DD HH:mm:ss") }}</pre>
  </div>
</template>

<script setup lang="ts">
  import {ref} from 'vue'
  import moment from "moment";

  const timestamp = ref(1183135260000);
</script>