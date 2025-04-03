<template>
  <n-button size="small" @click="onHandelClick" :disabled="props.column.isDisabled" v-if="!column.option">
    <template #icon>
      <n-icon>
        <SvgIcon :name="column.icon"/>
      </n-icon>
    </template>
    {{column.title}}
  </n-button>

  <n-tag checkable class="tag-item" @click="onHandelClick" :disabled="props.column.isDisabled"
         v-if="column.option"
  >
    <template #icon v-if="column.icon">
      <SvgIcon :name="column.icon" style="margin-right: 5px"/>
    </template>
    {{column.title}}
  </n-tag>
</template>

<script setup lang="ts">
  import {inject, onBeforeMount, onBeforeUpdate, type PropType, shallowRef} from 'vue'
  import {type RouteLocationResolved, useRouter} from "vue-router";
  /********************************************************************************
   * 路由列
   *
   * @author Berlin
   ********************************************************************************/
  import LinkColumnProps from "@/ploutos/graces/ag01/faces/columns/LinkColumnProps.ts";
  import SvgIcon from "@/ploutos/layouts/icons/SvgIcon.vue";
  import {Parser} from "expr-eval";

  /**
   * 路由
   */
  const router = useRouter();

  /**
   * link执行的组件
   */
  const component = shallowRef(undefined);

  /**
   * link中的通用界面模块号和名称
   */
  const module = shallowRef('');
  const name = shallowRef('');

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
  onBeforeMount(() => {
    disabled();
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
      props.column.isDisabled = Parser.parse(props.column.disabled).evaluate(props.column.rowData);
    }
  }

  /**
   * 按钮点击
   */
  async function onHandelClick() {
    if (props.column.isDisabled) {
      return;
    }
    const column = props.column;
    const value = column.rowData[column.name];

    if (props.column?.mode != 'router') {
      // 匹配路由（通用界面模式）
      const resolved: RouteLocationResolved = router.resolve(props.column?.link);
      let route: any = resolved.matched[resolved.matched.length - 1];

      // 匹配路由，正常模式
      if (!route.components) {
        const resolved: RouteLocationResolved = router.resolve(props.column?.link + "/" + value);
        route = resolved.matched[resolved.matched.length - 1];
      } else {
        module.value = resolved.params.module.toString();
        name.value = resolved.params.name.toString();
      }

      // 路由组件未加载时进行加载
      if (typeof(route?.components!.default) == 'function') {
        const components = await route?.components.default();
        component.value = components.default;
      } else {
        component.value = route?.components!.default!;
      }
    }

    const params = {
      value: value, module: module.value, name: name.value
    }
    if (column.mode == 'dialog') {
      onShowModal(column, component.value, params);
      return;
    }
    if (column.mode == 'drawer') {
      onShowDrawer(column, component.value, params);
      return;
    }
    if (column.mode == 'router') {
      onCloseContextMenu();

      // 将来源界面传入，用于编辑等界面刷新表格数据
      const name = props.column.moduleName;
      const from = props.column.module + name.substring(0, 1).toUpperCase() + name.substring(1);

      await router.push({
        path: column?.link! + "/" + value,
        query: {
          from: from
        }
      });
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