<template>
  <n-button type="primary" @click="onHandelClick" v-if="!props.action.option && !props.action.danger"
            :disabled="props.action.isDisabled"
  >
    <template #icon v-if="action.icon">
      <SvgIcon :name="action.icon"/>
    </template>
    {{action.title}}
  </n-button>

  <n-tag v-else :checkable="props.action.option" class="tag-item" :type="action.danger && 'error'"
         :style="{color: action.danger && '#e88080', cursor: 'pointer'}" @click="onHandelClick"
         :disabled="props.action.isDisabled"
  >
    <template #icon v-if="action.icon">
      <SvgIcon :name="action.icon" style="margin-right: 5px"/>
    </template>
    {{action.title}}
  </n-tag>
</template>

<script setup lang="ts">
  import {inject, onBeforeMount, onBeforeUpdate, type PropType} from 'vue'
  import {Parser} from "expr-eval";
  /********************************************************************************
   * 脚本动作按钮
   *
   * @author Berlin
   ********************************************************************************/
  import SvgIcon from "@/ploutos/layouts/icons/SvgIcon.vue";
  import type ScriptLinkActionProps from "@/ploutos/graces/ag01/faces/actions/ScriptLinkActionProps.ts";
  import {dialog, http, loading, message} from "@/ploutos";

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
   * 注入刷新函数
   */
  const onRefresh = inject<Function>('onRefresh');

  /**
   * 组件加载前
   */
  onBeforeMount(() => {
    disabled();
  });

  /**
   * 组件更新前
   */
  onBeforeUpdate(() => {
    disabled();
  })

  /**
   * 禁用
   */
  function disabled() {
    if (props.action.rowData && props.action.disabled) {
      props.action.isDisabled = Parser.parse(props.action.disabled).evaluate(props.action.rowData);
    }
  }

  /**
   * 按钮点击
   */
  function onHandelClick() {
    if (props.action.isDisabled) {
      return;
    }

    // 远程调用（浏览表单）
    if (props.action.rowData && props.action.mode == 'remote') {
      const label = props.action.rowData[props.action.labelColumn];

      // 确认弹框提示
      dialog.warning({
        content: '是否确认' + props.action.title + props.action.viewerTitle + ' - ' + label + '？',
        onConfirmClick: () => onConfirm(label)
      });
    }

    // 脚本执行（浏览表单）
    if (props.action.rowData && props.action.mode == 'script') {
      const func = new Function( 'link', 'data', 'return eval(link).call(this, data)');
      func(props.action.link, {
        value: props.action.rowData[props.action.name],
        rowData: props.action.rowData
      });
    }

    // 脚本执行（浏览表格）
    if (!props.action.rowData && props.action.mode == 'script') {
      const func = new Function( 'link', 'data', 'return eval(link).call(this, data)');
      func(props.action.link, {
        data: props.action.datatable.data,
        checkedKeys: props.action.datatable.checkedKeys,
      });
    }
  }

  /**
   * 调用后台
   */
  async function onConfirm(label: string) {
    try {
      loading(true);
      await http.post(props.action.link, [props.action.rowData['id']]);
      message.success(props.action.viewerTitle + '[ ' + label + ' ]' + props.action.title + '成功');
      onRefresh();
    } finally {
      loading(false);
    }
  }
</script>

<style scoped lang="scss">
  .tag-item {
    padding: 17px 9.5px;
    margin: 0 4px;
    width: calc(100% - 8px);
  }
</style>