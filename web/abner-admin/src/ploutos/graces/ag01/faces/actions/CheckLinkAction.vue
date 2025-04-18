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
import {h, inject, type PropType} from 'vue'
  /********************************************************************************
   * 选择项动作按钮
   *
   * @author Berlin
   ********************************************************************************/
  import SvgIcon from "@/ploutos/layouts/icons/SvgIcon.vue";
  import type CheckLinkActionProps from "@/ploutos/graces/ag01/faces/actions/CheckLinkActionProps.ts";
  import {http, dialog, message, loading} from "@/ploutos";
  import BatchDialogContent from "@/ploutos/graces/ag01/faces/actions/BatchDialogContent.vue";
  import type Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    action: {
      type: Object as PropType<CheckLinkActionProps>,
      required: true
    }
  });

  /**
   * 注入刷新函数
   */
  const onRefresh = inject<Function>('onRefresh');

  /**
   * 按钮点击
   */
  function onHandelClick() {
    // 检查是否选择了数据
    const datatable = props.action.datatable;
    if (!datatable.checkedKeys || datatable.checkedKeys.length <= 0) {
      message.error("请选择需要操作的数据");
      return;
    }

    // 远程调用
    if (props.action.mode == 'remote') {

      // 提示内容
      const content = [];
      datatable.checkedKeys.forEach(key => {
        const data = datatable.data.find(i => i[datatable.checkRowKey] == key);
        content.push(data[props.action.labelColumn]);
      });

      // 确认弹框提示
      dialog.warning({
        content: h(BatchDialogContent, {
          title: '是否确认' + props.action.title + '以下' + datatable.title + '？',
          content: content
        }),
        onConfirmClick: () => onConfirm(datatable)
      });
    }

    // 脚本执行
    if (props.action.mode == 'script') {
      const func = new Function( 'link', 'data', 'return eval(link).call(this, data)');
      func(props.action.link, {
        data: datatable.data, checkedKeys: datatable.checkedKeys
      });
    }
  }

  /**
   * 调用后台
   */
  async function onConfirm(datatable: Datatable) {
    try {
      loading(true);
      await http.post(props.action.link, datatable.checkedKeys);
      message.success(datatable.title + props.action.title + '成功');
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