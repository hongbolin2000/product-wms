<template>
  <n-button type="primary" @click="onHandelClick" v-if="!props.action.option">
    <template #icon v-if="action.icon">
      <SvgIcon :name="action.icon"/>
    </template>
    {{action.title}}
  </n-button>

  <n-tag v-else checkable class="tag-item" :type="action.danger && 'error'"
         :style="{color: action.danger && '#e88080'}" @click="onHandelClick"
  >
    <template #icon v-if="action.icon">
      <SvgIcon :name="action.icon" style="margin-right: 5px"/>
    </template>
    {{action.title}}
  </n-tag>
</template>

<script setup lang="ts">
  import {type PropType} from 'vue'
  /********************************************************************************
   * 脚本动作按钮
   *
   * @author Berlin
   ********************************************************************************/
  import SvgIcon from "@/ploutos/layouts/icons/SvgIcon.vue";
  import type ScriptLinkActionProps from "@/ploutos/graces/ag01/faces/actions/ScriptLinkActionProps.ts";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    action: {
      type: Object as PropType<ScriptLinkActionProps>,
      required: true
    }
  });

  /**
   * 按钮点击
   */
  function onHandelClick() {
    const func = new Function( 'link', 'return eval(link).call()');
    func(props.action.link);
  }
</script>

<style scoped lang="scss">
  .tag-item {
    padding: 17px 9.5px;
    margin: 0 4px;
    width: calc(100% - 8px);
  }
</style>