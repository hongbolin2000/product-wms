<template>
  <n-tag checkable class="tag-item" @click="onHandelClick">
    <template #icon>
      <SvgIcon :name="icon" style="margin-right: 5px"/>
    </template>
    {{column.title}}
  </n-tag>
</template>

<script setup lang="ts">
import {computed, inject, onMounted, type PropType, shallowRef} from 'vue'
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
  const onCloseContextMenu = inject('onCloseContextMenu');

  /**
   * 注入模态框显示函数
   */
  const onShowModal = inject('onShowModal');

  /**
   * 注入抽屉显示函数
   */
  const onShowDrawer = inject('onShowDrawer');

  /**
   * 组件加载
   */
  onMounted(async () => {
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
   * 按钮图标
   */
  const icon = computed(() => {
    if (props.column.icon == 'edit') {
      return 'file-text';
    }
    return props.column.icon;
  });

  /**
   * 按钮点击
   */
  function onHandelClick() {
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
    padding: 15px 10px;
    margin: 0 5px;
    width: calc(100% - 10px);
  }
</style>