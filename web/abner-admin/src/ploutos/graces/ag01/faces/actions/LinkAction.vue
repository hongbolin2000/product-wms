<template>
    <n-button type="primary" @click="onHandelClick">
      <template #icon v-if="action.icon">
        <SvgIcon :name="action.icon"/>
      </template>
      {{action.title}}
    </n-button>

    <n-modal
      v-model:show="showModal"
      :title="title"
      :draggable="{bounds: 'none'}"
      :style="{width: props.action?.dialogWidth}"
      preset="dialog"
      :mask-closable="false"
      :close-on-esc="false"
    >
      <component :is="component" :params="{module: module, name: name, id: id}" :is-dialog="true"
                 @on-close="showModal = false" @on-refresh="handleRefresh" @on-change-title="(value) => title = value"
      />
    </n-modal>

    <n-drawer v-model:show="showDrawer" :width="action.drawerWidth" placement="right" :mask-closable="false" :close-on-esc="false">
      <n-drawer-content :title="title" closable :body-content-style="{padding: 0}">
        <component :is="component" :params="{module: module, name: name, id: id}" :is-drawer="true"
                   @on-close="showDrawer = false" @on-refresh="handleRefresh" @on-change-title="(value) => title = value"
        />
      </n-drawer-content>
    </n-drawer>
</template>

<script setup lang="ts">
import {computed, inject, onMounted, type PropType, ref, shallowRef} from 'vue'
import {type RouteLocationResolved, useRouter} from "vue-router";
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
   * link中的通用界面模块号和名称
   */
  const module = shallowRef('');
  const name = shallowRef('');

  /**
   * link中的id（用于dialog和drawer）
   */
  const id = shallowRef('');

  /**
   * 新增/编辑时的弹框标题
   */
  const title = shallowRef('');

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
   * 注入刷新函数
   */
  const onRefresh = inject<Function>('onRefresh');

  /**
   * 组件加载
   */
  onMounted(async () => {
    if (props.action?.mode != 'router') {
      let route: any = router.getRoutes().find(i => i.path == props.action?.link);

      // 找不到路由时使用路由匹配（通用界面模式）
      if (!route) {
        const resolved: RouteLocationResolved = router.resolve(props.action?.link);
        route = resolved.matched[resolved.matched.length - 1];
        module.value = resolved.params.module.toString();
        name.value = resolved.params.name.toString();

        // dialog/drawer模式（浏览表单界面）
        if (resolved.params.id) {
          id.value = resolved.params.id.toString();
        }
      }

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

    // 将来源界面传入，用于编辑等界面刷新表格数据
    const name = props.action.moduleName;
    const from = props.action.module + name.substring(0, 1).toUpperCase() + name.substring(1);
    router.push({
      path: props.action.link,
      query: {
        from: from
      }
    });
  }

  /**
   * 查询
   */
  function handleRefresh() {
    onRefresh();
  }
</script>

<style scoped lang="scss">

</style>