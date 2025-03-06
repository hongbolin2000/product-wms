<template>
    <n-button type="primary">
      <template #icon>
        <n-icon v-if="action.icon == 'add'">
          <Add/>
        </n-icon>
      </template>
      {{action.label}}
    </n-button>
</template>

<script setup lang="ts">
  import {type PropType, ref} from 'vue'
  import {Add} from '@vicons/ionicons5';
  /********************************************************************************
   * 路由动作按钮
   *
   * @author Berlin
   ********************************************************************************/
  import LinkActionProps from "@/ploutos/graces/ag01/faces/actions/LinkActionProps.ts";

  /**
   * 父组件传入的属性
   */
  defineProps({
    action: {
      type: Object as PropType<LinkActionProps>,
      required: true
    }
  });
</script>

<style scoped lang="scss">

</style>