<template>
    <n-button type="primary" @click="onHandelClick">
      <template #icon v-if="action.icon">
        <SvgIcon :name="action.icon"/>
      </template>
      {{action.title}}
    </n-button>

    <n-modal
      v-model:show="showModal"
      :title="action.title"
      :draggable="{bounds: 'none'}"
      :style="{width: props.action?.dialogWidth}"
      preset="dialog"
    >
      <component :is="component" :is-dialog="true" @on-close="showModal = false"/>
    </n-modal>

    <n-drawer v-model:show="showDrawer" :width="action.drawerWidth" placement="right">
      <n-drawer-content :title="action.title" closable :body-content-style="{padding: 0}">
        <component :is="component" :is-drawer="true" @on-close="showDrawer = false"/>
      </n-drawer-content>
    </n-drawer>
</template>

<script setup lang="ts">
import {computed, onMounted, type PropType, ref, shallowRef} from 'vue'
  import {useRouter} from "vue-router";
  /********************************************************************************
   * 路由动作按钮
   *
   * @author Berlin
   ********************************************************************************/
  import LinkActionProps from "@/ploutos/graces/ag01/faces/actions/LinkActionProps.ts";
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
   * 是否显示模态弹框
   */
  const showModal = ref(false);

  /**
   * 是否显示抽屉
   */
  const showDrawer = ref(false);

  /**
   * 父组件传入的属性
   */
  const props = defineProps({
    action: {
      type: Object as PropType<LinkActionProps>,
      required: true
    }
  });

  /**
   * 按钮图标
   */
  const icon = computed(() => {
    return props.action.icon;
  });

  /**
   * 组件加载
   */
  onMounted(async () => {
    if (props.action?.mode != 'router') {
      const route: any = router.getRoutes().find(i => i.path == props.action?.link);

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
   * 按钮点击
   */
  function onHandelClick() {
    if (props.action?.mode == 'dialog') {
      showModal.value = true;
      return;
    }
    if (props.action?.mode == 'drawer') {
      showDrawer.value = true;
      return;
    }
    router.push(props.action?.link!);
  }
</script>

<style scoped lang="scss">

</style>