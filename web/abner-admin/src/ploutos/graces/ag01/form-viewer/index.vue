<template>
  <n-spin :show="props.spining ? props.spining : spining" style="height: 100%">
    <template #icon>
      <n-icon>
        <SettingsOutline />
      </n-icon>
    </template>

    <div v-if="viewer">
      <n-card
          style="margin-bottom: 10px"
          :bordered="!props.isDrawer && !props.isDialog"
          :content-style="{paddingBottom: (props.isDrawer || props.isDialog) && 0}"
      >
        <n-space :size="10">
          <component v-for="action of actions" :key="action.name" :is="() => {
              action.module = props.module;
              action.moduleName = props.name;
              action.rowData = viewerValue;
              action.viewerTitle = viewer.title;
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

          <n-button @click="onClose()">关闭页面</n-button>
          <n-button @click="loadData()">刷新数据</n-button>
          <n-button @click="layoutStore.fullScreen()">全屏显示</n-button>
        </n-space>
      </n-card>

      <n-scrollbar :style="style">
        <!-- 浏览表单 -->
        <div v-for="(row, index) of viewer.viewerRows" :key="index"
             :class="[props.isDrawer || props.isDialog ? 'viewer-row-drawer' : 'viewer-row']"
        >
          <!-- 非选项卡表单 -->
          <n-card
              :title="formViewer.title"
              :segmented="{content: true}"
              v-for="formViewer of row.noTabViewers" :key="formViewer.name"
              :style="{width: getCardWidth(row.tabViewers, row.noTabViewers, false, formViewer)}"
              :bordered="!props.isDrawer && !props.isDialog"
              :content-style="{paddingBottom: (props.isDrawer || props.isDialog) && 0}"
          >
            <template #header-extra>
              <n-button @click="onCollapse(row)" text>
                {{formViewer.collapse ? '展开' : '折叠'}}
              </n-button>
            </template>

            <n-collapse-transition :show="!formViewer.collapse">
              <viewer-grid :viewer="formViewer" :viewer-value="viewerValue" :viewer-count="row.viewerCount"/>
            </n-collapse-transition>
          </n-card>

          <!-- 选项卡表单 -->
          <n-card
              v-if="row.tabViewers.length > 0"
              class="n-card-tab"
              :style="{width: getCardWidth(row.tabViewers, row.noTabViewers, false)}"
              :bordered="!props.isDrawer && !props.isDialog"
              :content-style="{paddingBottom: (props.isDrawer || props.isDialog) && 0}"
          >
            <n-tabs type="line" animated>
              <template #suffix>
                <n-button @click="onCollapse(row)" text>
                  {{row.tabViewers[0].collapse ? '展开' : '折叠'}}
                </n-button>
              </template>

              <n-tab-pane :name="viewer.name" :tab="viewer.title" v-for="viewer of row.tabViewers" :key="viewer.name">
                <n-collapse-transition :show="!viewer.collapse">
                  <viewer-grid :viewer="viewer" :viewer-value="viewerValue" :viewer-count="row.viewerCount"/>
                </n-collapse-transition>
              </n-tab-pane>
            </n-tabs>
          </n-card>
        </div>

        <!-- 浏览表格 -->
        <div v-for="(row, index) of viewer.datatableRows" :key="index"
             :class="[props.isDrawer || props.isDialog ? 'viewer-row-drawer' : 'viewer-row']"
        >
          <!-- 非选项卡表格 -->
          <n-card
              :title="datatable.title"
              v-for="datatable of row.noTabDatatables" :key="datatable.name"
              :style="{width: getCardWidth(row.tabDatatables, row.noTabDatatables, true, datatable)}"
              :bordered="!props.isDrawer && !props.isDialog"
              :content-style="{paddingBottom: (props.isDrawer || props.isDialog) && 0}"
              :segmented="{content: true}"
          >
            <template #header-extra>
              <n-button @click="onDatatableCollapse(row)" text>
                {{datatable.collapse ? '展开' : '折叠'}}
              </n-button>
            </template>

            <n-collapse-transition :show="!datatable.collapse">
              <DataTable
                  :main-data-table="datatable"
                  :datatable="datatable"
                  :max-height="datatable.maxHeight"
                  @on-search="loadData"
                  @on-sort="handelSort"
                  :module="props.module"
                  :module-name="props.name"
              />
            </n-collapse-transition>
          </n-card>

          <!-- 选项卡表格 -->
          <n-card
              v-if="row.tabDatatables.length > 0"
              :class="['n-card-tab', props.isDrawer || props.isDialog ? 'viewer-row-drawer' : 'viewer-row']"
              :bordered="!props.isDrawer && !props.isDialog"
              :content-style="{paddingBottom: (props.isDrawer || props.isDialog) && 0}"
              :style="{width: getCardWidth(row.tabDatatables, row.noTabDatatables, true)}"
          >
            <n-tabs type="line" animated>
              <template #suffix>
                <n-space :size="10">
                  <n-button @click="onDatatableCollapse(row)" text>
                    {{row.tabDatatables[0].collapse ? '展开' : '折叠'}}
                  </n-button>
                </n-space>
              </template>

              <n-tab-pane :name="datatable.name" :tab="datatable.title" v-for="datatable of row.tabDatatables" :key="datatable.name">
                <n-collapse-transition :show="!datatable.collapse">
                  <DataTable
                      :main-data-table="datatable"
                      :datatable="datatable"
                      :max-height="datatable.maxHeight"
                      @on-search="loadData"
                      @on-sort="handelSort"
                      :module="props.module"
                      :module-name="props.name"
                  />
                </n-collapse-transition>
              </n-tab-pane>
            </n-tabs>
          </n-card>
        </div>
      </n-scrollbar>
    </div>
  </n-spin>
</template>

<script setup lang="ts">
import {computed, onMounted, provide, ref, type Ref, shallowRef} from "vue";
  import {useRoute} from "vue-router";
  /********************************************************************************
   * 通用浏览表单界面
   *
   * @author Berlin
   ********************************************************************************/
  import useAppStore from "@/ploutos/layouts/store/app-store.ts";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store.ts";
  import type ViewerProps from "@/ploutos/graces/ag01/form-viewer/ViewerProps.ts";
  import {http, loading} from "@/ploutos";
  import type FormViewer from "@/ploutos/graces/ag01/faces/FormViewer.ts";
  import type Datatable from "@/ploutos/graces/ag01/faces/Datatable.ts";
  import FormViewerRow from "@/ploutos/graces/ag01/faces/FormViewerRow.ts";
import {ChevronDown, SettingsOutline} from "@vicons/ionicons5";
  import ViewerGrid from "@/ploutos/graces/ag01/form-viewer/components/ViewerGrid.vue";
  import type DatatableRow from "@/ploutos/graces/ag01/faces/DatatableRow.ts";
  import DataTable from "@/ploutos/graces/ag01/components/DataTable.vue";
  import ActionFactories from "@/ploutos/graces/ag01/faces/ActionFactories.ts";
  import type AbstractAction from "@/ploutos/graces/ag01/faces/AbstractAction.ts";
  import {type DropdownOption, NIcon} from "naive-ui";

  /**
   * 当前路由
   */
  const route = useRoute();

  /**
   * 来源界面
   */
  const fromPage = shallowRef('');

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
   * 展示按钮
   */
  const actions: Ref<AbstractAction[]> = shallowRef([]);
  const optionActions: Ref<DropdownOption[]> = shallowRef([]);

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
   * 查询条件
   */
  const params: Ref = ref(props.params);

  /**
   * 浏览表单数据
   */
  const viewerValue: Ref = ref(new Object({}));

  /**
   * 友好提示提示的列
   */
  const labelColumn = ref('');

  /**
   * 排序字段
   */
  const sorter: Ref = ref(null);

  /**
   * 组件加载
   */
  onMounted(async () => {
    // 将刷新事件写入window
    const name = props.module + props.name.substring(0, 1).toUpperCase() + props.name.substring(1) + "Refresh";
    window[name] = onRefreshViewer;

    if (!props.isDialog && !props.isDrawer) {
      fromPage.value = route.query.from.toString();
    }
    await loadDefine();

    // 更多操作
    actions.value = viewer.value.actions.filter(i => !i.option);
    const options = viewer.value.actions.filter(i => i.option);
    const actionOptions: DropdownOption[] = []
    options.forEach(action => {
      actionOptions.push({
        key: action.name,
        type: 'render',
        render: () => {
          action.module = props.module;
          action.moduleName = props.name;
          action.rowData = viewerValue.value;
          action.viewerTitle = viewer.value.title;
          return ActionFactories.getInstance().create(action);
        },
      });
    });
    optionActions.value = actionOptions;
  });

  /**
   * 加载中
   */
  function spin(value: boolean) {
    if (props.isDialog || props.isDrawer) {
      spining.value = value;
    } else  {
      loading(value);
    }
  }

  /**
   * 加载界面定义
   */
  async function loadDefine() {
    try {
      spin(true);

      // 查询界面定义
      const data = {
        module: props.module, name: props.name, params: params.value, local: navigator.language
      }
      const response = await http.post("/ag01/viewer/loadDefine", data);
      viewer.value = response.data;

      // 表单显示行
      const allViewers: FormViewer[] = [];
      viewer.value.viewerRows.forEach(row => {
        // 选项卡表单
        row.tabViewers = row.viewers.filter(i => i.tab);
        row.noTabViewers = row.viewers.filter(i => !i.tab);

        // 加入全部表单
        allViewers.push(...row.noTabViewers);
        allViewers.push(...row.tabViewers);

        // 计算一行展示的表单数
        let viewerCount = row.noTabViewers.length;
        viewerCount += row.tabViewers.length > 0 ? 1 : 0;
        row.viewerCount = viewerCount;
      });
      labelColumn.value = allViewers[0].labelColumn;

      // 拆分浏览表格
      viewer.value.datatableRows.forEach(row => {
        row.tabDatatables = row.datatables.filter(i => i.tab);
        row.noTabDatatables = row.datatables.filter(i => !i.tab);
      });

      // 初始化字段值
      const value =  {};
      allViewers.forEach(viewer => {
        viewer.scenes.forEach(scene => {
          value[scene.name] = '';
        });

        // 过滤掉隐藏的控件
        viewer.scenes = viewer.scenes.filter(i => !i.hidden);
      });
      viewerValue.value = value;

      await loadData();

      // 更改选型卡标题
      const title = viewer.value.title + ' - ' + viewerValue.value[labelColumn.value];
      if (!props.isDialog && !props.isDrawer) {
        appStore.changeTabTitle(title);
      } else {
        emit('onChangeTitle', title);
      }
    } finally {
      spin(false);
    }
  }

  /**
   * 加载界面数据
   */
  async function loadData() {
    try {
      spin(true);

      const data = {
        module: props.module, name: props.name, local: navigator.language,
        params: params.value, sorter: sorter.value
      }
      const response = await http.post("/ag01/viewer/loadData", data);
      if (response.data.viewer) {
        viewerValue.value = response.data.viewer;
      }

      // 编辑表格数据
      let index = -1;
      viewer.value.datatableRows.forEach(row => {
        row.datatables.forEach(datatable => {
          index++;
          datatable.data = response.data.datatable[index]
        });
      });
    } finally {
      spin(false);
    }
  }
  provide('params', params.value);

  /**
   * 操作后刷新
   */
  async function onRefreshViewer() {
    await loadData();

    // 刷新上层界面数据
    if (props.isDialog || props.isDrawer) {
      emit('onRefresh');
    } else {
      const func = new Function( 'name', 'return eval(name).call()');
      func(fromPage.value + 'Refresh');
    }
  }
  provide('onRefresh', onRefreshViewer);

  /**
   * 关闭
   */
  function onClose() {
    if (props.isDialog || props.isDrawer) {
      emit('onClose');
    } else {
      appStore.closeCurrentTab();
    }
  }

  /**
   * 计算表单/表格面板宽度
   */
  function getCardWidth(
      tabViewers: FormViewer[] | Datatable[], noTabViewers: FormViewer[] | Datatable[],
      isDatatable: boolean, viewer?: FormViewer | Datatable
  ) {

    // 如果是浏览表格则需要减去gap
    let gap = 0;
    if (isDatatable) {
      let viewerCount = noTabViewers.length;
      viewerCount += tabViewers.length > 0 ? 1 : 0;
      gap = 10 * (viewerCount - 1) / viewerCount;
    }

    // 非选项卡，设置了宽度
    if (viewer && viewer.width) {
      return 'calc(' + viewer.width + ' - ' + gap + 'px)';
    }

    // 选项卡，设置了宽度
    if (!viewer && tabViewers[0].width) {
      return 'calc(' + tabViewers[0].width + ' - ' + gap + 'px)';
    }

    // 未设置宽度，由100%减去当前行设置了的选项卡宽度
    let restWidth = "100% ", restCount = 0;
    if (tabViewers.length > 0) {
      if (tabViewers[0].width) {
        restWidth += "- " + tabViewers[0].width;
      } else {
        restCount++;
      }
    }

    // 未设置宽度，由100%减去当前行设置了的面板宽度
    // @ts-ignore
    noTabViewers.filter(i => !i.tab).forEach(viewer => {
      if (viewer.width) {
        restWidth += "- " + viewer.width;
      } else {
        restCount++;
      }
    });

    // 未设置宽度的表单宽度
    return 'calc((' + restWidth + ') / ' + restCount + ' - ' + gap + 'px)';
  }

  /**
   * 页面高度
   */
  const style = computed(() => {
    // 路由页面展示
    if (!props.isDialog && !props.isDrawer) {
      if (layoutStore.isFullScreen) {
        return {height: 'calc(100vh - var(--top-option-height) - 10px)'};
      }
      return {height: 'calc(100vh - var(--logo-height) - var(--tab-height) - var(--top-option-height))'};
    }
    // 抽屉展示
    if (props.isDrawer) {
      return {height: 'calc(100vh - var(--top-option-height) - 30px)'};
    }
    // 弹框展示
    if (props.isDialog) {
      return {maxHeight: '60vh'};
    }
  });

  /**
   * 折叠面板
   */
  function onCollapse(row: FormViewerRow) {
    row.viewers.forEach(editor => {
      editor.collapse = !editor.collapse
    });
  }

  /**
   * 折叠浏览表格
   */
  function onDatatableCollapse(row: DatatableRow) {
    row.datatables.forEach(datatable => {
      datatable.collapse = !datatable.collapse;
    });
  }

  /**
   * 表格排序
   */
  function handelSort(state: any) {
    sorter.value = state;
    loadData();
  }
</script>

<style scoped lang="scss">
  .viewer-row, .viewer-row-drawer {
    display: flex;
    gap: 10px;
  }
  .viewer-row + .viewer-row {
    margin-top: 10px;
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
</style>