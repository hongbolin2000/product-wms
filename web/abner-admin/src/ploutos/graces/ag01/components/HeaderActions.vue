<template>
  <div class="header-wrapper">
    <n-scrollbar x-scrollable>
      <n-space :size="10" class="action-wrapper">
        <component v-for="action of actions" :key="action.name" :is="() => {
            action.datatable = datatable;
            action.module = props.module;
            action.moduleName = props.moduleName;
            return ActionFactories.getInstance().create(action)
        }"/>

        <n-dropdown trigger="hover" :options="optionActions" v-if="optionActions.length > 0">
          <n-button icon-placement="right" class="more-action">
            <template #icon>
              <n-icon class="tip">
                <ChevronDown style="font-size: 14px"/>
              </n-icon>
            </template>
            更多操作
          </n-button>
        </n-dropdown>

        <n-button @click="appStore.closeCurrentTab()">关闭页面</n-button>
        <n-button @click="showFilterBar = !showFilterBar">查询条件</n-button>
        <n-divider vertical style="margin: 0"/>

        <n-select
            v-for="column of enumFilterColumns"
            :style="{width: '108px'}"
            :options="column.filterOptions"
            :clearable="true"
            :placeholder="column.title"
            v-model:value="params[column?.name]"
            :onUpdate:value="onSearch"
        ></n-select>
      </n-space>
    </n-scrollbar>
  </div>

  <!-- 查询条件 -->
  <n-drawer v-model:show="showFilterBar" :width="500" placement="right">
    <n-drawer-content title="查询条件" closable :native-scrollbar="false">
      <HeaderFilterBar :columns="datatable.columns"/>

      <template #footer>
        <n-space :size="10">
          <n-button @click="showFilterBar = false">关闭</n-button>
          <n-button @click="() => {
            datatable.columns.forEach(column => {
              params[column.name] = null;
            });
            onSearch();
          }">重置</n-button>
          <n-button type="primary" @click="onSearch">查询</n-button>
        </n-space>
      </template>
    </n-drawer-content>
  </n-drawer>
</template>

<script setup lang="ts">
  import {ChevronDown} from "@vicons/ionicons5";
  import {type DropdownOption, NIcon} from "naive-ui";
  import {inject, onBeforeMount, type PropType, provide, ref, type Ref, shallowRef} from "vue";
  /********************************************************************************
   * 表格头部操作按钮
   *
   * @author Berlin
   ********************************************************************************/
  import ActionFactories from "@/ploutos/graces/ag01/faces/ActionFactories.ts";
  import type AbstractAction from "@/ploutos/graces/ag01/faces/AbstractAction.ts";
  import type Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";
  import type AbstractColumn from "@/ploutos/graces/ag01/faces/AbstractColumn.ts";
  import useAppStore from "@/ploutos/layouts/store/app-store.ts";
  import HeaderFilterBar from "@/ploutos/graces/ag01/components/HeaderFilterBar.vue";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store.ts";

  /**
   * 应用状态
   */
  const appStore = useAppStore();
  const layoutStore = useLayoutStore();

  /**
   * 父组件传入属性
   */
  const props = defineProps({
    datatable: {
      type: Object as PropType<Datatable>,
      required: true
    },
    module: {
      type: String,
      required: true,
    },
    moduleName: {
      type: String,
      required: true,
    }
  });

  /**
   * 父组件事件
   */
  const emit = defineEmits<{

    /**
     * 条件查询
     */
    (e: 'onSearch'): void;
  }>();

  /**
   * 注入查询参数
   */
  const params: Ref = ref(inject('params'));

  /**
   * 展示按钮
   */
  const actions: Ref<AbstractAction[]> = shallowRef([]);
  const optionActions: Ref<DropdownOption[]> = shallowRef([]);

  /**
   * 枚举过滤
   */
  const showFilterBar = ref(false);
  const enumFilterColumns: Ref<AbstractColumn[]> = ref([]);

  /**
   * 组件加载
   */
  onBeforeMount(() => {
    if (props.datatable.actions) {
      actions.value = props.datatable.actions.filter(i => !i.option);
      const options = props.datatable.actions.filter(i => i.option);

      const actionOptions: DropdownOption[] = []
      options.forEach(action => {
        actionOptions.push({
          key: action.name,
          type: 'render',
          render: () => {
            action.datatable = props.datatable;
            return ActionFactories.getInstance().create(action);
          },
        });
      });
      optionActions.value = actionOptions;
    }

    // 枚举过滤
    enumFilterColumns.value = props.datatable.columns.filter(i => i.filterOptions);
  });

  /**
   * 条件搜索
   */
  function onSearch() {
    emit('onSearch');

    if (layoutStore.closeOnSearch) {
      showFilterBar.value = false;
    }
  }
  provide('onSearch', onSearch);
</script>

<style scoped lang="scss">
  .header-wrapper {
    flex: 1;
    overflow: hidden;
    margin-right: 10px;

    .action-wrapper {
      align-items: center;
      flex-wrap: nowrap
    }

    .more-action {
      .tip {
        transition: transform $transitionTime;
      }
      &:hover {
        .tip {
          transform: rotate(180deg);
        }
      }
    }
  }
</style>