<template>
  <n-space>
    <div v-for="(action, index) of permissionActions" :key="action.actionCode">
      <n-checkbox v-model:checked="action.assigned" @update-checked="calCheckedKeys()">
        {{action.actionName}}
      </n-checkbox>
      <n-divider vertical v-if="index + 1 != permissionActions.length"/>
    </div>
  </n-space>
</template>

<script setup lang="ts">
  import type {PropType} from "vue";
  /********************************************************************************
   * 权限动作选择列
   *
   * @author Berlin
   ********************************************************************************/
  import type {PermissionAction} from "@/views/sy/sy01/types.ts";

  /**
   * 父组件传入的属性
   */
  defineProps({
    permissionActions: {
      type: Object as PropType<PermissionAction[]>,
      required: true
    },
    calCheckedKeys: {
      type: Function,
      required: true
    }
  });
</script>

<style scoped lang="scss">

</style>