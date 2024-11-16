<template>
  <n-config-provider :theme="lightTheme">
    <router-view/>
  </n-config-provider>
</template>

<script setup lang="ts">
import { lightTheme } from 'naive-ui'
</script>
