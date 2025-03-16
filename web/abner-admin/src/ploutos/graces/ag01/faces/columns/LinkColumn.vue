<template>
  <n-tag checkable class="tag-item" @click="onHandelClick" :disabled="props.column.isDisabled">
    <template #icon v-if="column.icon">
      <SvgIcon :name="column.icon" style="margin-right: 5px"/>
    </template>
    {{column.title}}
  </n-tag>
</template>

<script setup lang="ts">
  import {inject, onBeforeMount, onBeforeUpdate, type PropType, shallowRef} from 'vue'
  import {useRouter} from "vue-router";
  /********************************************************************************
   * 路由列
   *
   * @author Berlin
   ********************************************************************************/
  import LinkColumnProps from "@/ploutos/graces/ag01/faces/columns/LinkColumnProps.ts";
  import SvgIcon from "@/ploutos/layouts/icons/SvgIcon.vue";

  /**
   * 路由
   */
  const router = useRouter();

  /**
   * link执行的组件
   */
  const component = shallowRef(undefined);

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    column: {
      type: Object as PropType<LinkColumnProps>,
      required: true
    }
  });

  /**
   * 注入关闭右键菜单函数
   */
  const onCloseContextMenu = inject<Function>('onCloseContextMenu');

  /**
   * 注入模态框显示函数
   */
  const onShowModal = inject<Function>('onShowModal');

  /**
   * 注入抽屉显示函数
   */
  const onShowDrawer = inject<Function>('onShowDrawer');

  /**
   * 组件加载前
   */
  onBeforeMount(async () => {
    disabled();

    if (props.column?.mode != 'router') {
      const route: any = router.getRoutes().find(i => i.path == props.column?.link);

      // 路由组件未加载时进行加载
      if (typeof(route?.components!.default) == 'function') {
        const components = await route?.components.default();
        component.value = components.default;
      } else {
        component.value = route?.components!.default!;
      }
    }
  });

  /**
   * 组件更新前
   */
  onBeforeUpdate(() => {
    disabled();
  });

  /**
   * 禁用
   */
  function disabled() {
    if (props.column.disabled) {
      const func = new Function( 'rowData', 'disabled', 'return eval("rowData." + disabled)');
      props.column.isDisabled = func(props.column.rowData, props.column.disabled);
    }
  }

  /**
   * 按钮点击
   */
  function onHandelClick() {
    if (props.column.isDisabled) {
      return;
    }

    const column = props.column;
    const value = column.rowData[column.name];

    if (column.mode == 'dialog') {
      onShowModal(column, component.value, value);
      return;
    }
    if (column.mode == 'drawer') {
      onShowDrawer(column, component.value, value);
      return;
    }
    onCloseContextMenu();
    router.push(column?.link! + "/" + value);
  }
</script>

<style scoped lang="scss">
  .tag-item {
    padding: 17px 9.5px;
    margin: 0 4px;
    width: calc(100% - 8px);
  }
</style>