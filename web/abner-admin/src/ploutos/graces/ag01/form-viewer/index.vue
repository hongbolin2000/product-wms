<template>

</template>

<script setup lang="ts">
  import {ref, type Ref, shallowRef} from "vue";
  /********************************************************************************
   * 通用浏览表单界面
   *
   * @author Berlin
   ********************************************************************************/
  import useAppStore from "@/ploutos/layouts/store/app-store.ts";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store.ts";
  import type ViewerProps from "@/ploutos/graces/ag01/form-viewer/ViewerProps.ts";

  /**
   * 应用状态
   */
  const appStore = useAppStore();
  const layoutStore = useLayoutStore();

  /**
   * 是否加载中（提供给drawer和dialog使用）
   */
  const spining = shallowRef(false);

  /**
   * 浏览表单界面属性定义
   */
  const viewer: Ref<ViewerProps> = ref();


  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    // 模块号
    module: {
      type: String,
      required: true
    },
    // 界面名称
    name: {
      type: String,
      required: true
    },
    // 传入参数
    params: {
      type: Object
    },
    // 是否弹框展示
    isDialog: {
      type: Boolean
    },
    // 是否抽屉展示
    isDrawer: {
      type: Boolean
    },
    // 功能控制是否加载中
    spining: {
      type: Boolean
    }
  });


  /**
   * 父组件事件
   */
  const emit = defineEmits<{

    /**
     * 关闭弹框(dialog和drawer)
     */
    (e: 'onClose'): void;

    /**
     * 刷新上层数据
     */
    (e: 'onRefresh'): void;

    /**
     * 修改标题(drawer/dialog)
     */
    (e: 'onChangeTitle', title: string): void;
  }>();

  /**
   * 浏览表单数据
   */
  const formValue: Ref = ref(new Object({}));
</script>

<style scoped lang="scss">

</style>