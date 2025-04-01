<template>
  <n-button
      :text="column.danger"
      size="small"
      @click="onHandelClick"
      :disabled="props.column.isDisabled"
      v-if="!column.option"
      :type="column.danger ? 'error' : 'default'"
  >
    <template #icon v-if="column.icon">
      <n-icon>
        <SvgIcon :name="column.icon"/>
      </n-icon>
    </template>
    {{column.title}}
  </n-button>

  <n-tag
      checkable
      class="tag-item"
      @click="onHandelClick"
      :type="column.danger ? 'error' : 'default'"
      :style="{color: column.danger && '#e88080', padding: !column.option && 1}"
      :disabled="column.isDisabled"
      v-if="column.option"
  >
    <template #icon v-if="column.icon">
      <SvgIcon :name="column.icon" style="margin-right: 5px"/>
    </template>
    {{column.title}}
  </n-tag>
</template>

<script setup lang="ts">
  import {inject, onBeforeMount, onBeforeUpdate, type PropType} from 'vue'
  import {useRouter} from "vue-router";
  /********************************************************************************
   * 脚本按钮列
   *
   * @author Berlin
   ********************************************************************************/
  import SvgIcon from "@/ploutos/layouts/icons/SvgIcon.vue";
  import {dialog, http, loading, message} from "@/ploutos";
  import ScriptLinkColumnProps from "@/ploutos/graces/ag01/faces/columns/ScriptLinkColumnProps.ts";
  import {Parser} from "expr-eval";

  /**
   * 路由
   */
  const router = useRouter();

  /**
   * 注入关闭右键菜单函数
   */
  const onCloseContextMenu = inject<Function>('onCloseContextMenu');

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    column: {
      type: Object as PropType<ScriptLinkColumnProps>,
      required: true
    }
  });

  /**
   * 注入查询函数
   */
  const onSearch = inject<Function>('onSearch');

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
    if (props.column.disabled) {
      props.column.isDisabled = Parser.parse(props.column.disabled).evaluate(props.column.rowData);
    }
  }

  /**
   * 按钮点击
   */
  function onHandelClick() {
    if (props.column.isDisabled) {
      return;
    }
    onCloseContextMenu();

    // 远程调用
    if (props.column.mode == 'remote') {
      const label = props.column.rowData[props.column.labelColumn];

      // 确认弹框提示
      dialog.warning({
        content: '是否确认' + props.column.title + props.column.datatableTitle + ' - ' + label + '？',
        onConfirmClick: () => onConfirm(label)
      });
    }

    // 脚本执行
    if (props.column.mode == 'script') {
      const func = new Function( 'link', 'data', 'return eval(link).call(this, data)');
      func(props.column.link, {
        value: props.column.rowData[props.column.name],
        rowData: props.column.rowData,
        rowIndex: props.column.rowIndex
      });
    }
  }

  /**
   * 调用后台
   */
  async function onConfirm(label: string) {
    try {
      loading(true);
      await http.post(props.column.link, [props.column.rowData[props.column.name]]);
      message.success(props.column.datatableTitle + '[ ' + label + ' ]' + props.column.title + '成功');
      onSearch();
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