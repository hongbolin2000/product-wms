<template>
  <div>
    <div :style="style">
      <n-scrollbar>
        <!-- 编辑表单 -->
        <div v-for="row of editor.editorRows" :class="[props.isDrawer ? 'editor-row-drawer' : 'editor-row']">
          <!-- 非选项卡表单 -->
          <n-card
              :title="editor.title"
              :segmented="{content: true}"
              v-for="editor of row.noTabEditors"
              :style="{width: getCardWidth(row.tabEditors, row.noTabEditors, false, editor)}"
              :bordered="!props.isDrawer"
              :content-style="{paddingBottom: props.isDrawer && 0}"
          >
            <template #header-extra>
              <n-button @click="onCollapse(row)" text>
                {{editor.collapse ? '展开' : '折叠'}}
              </n-button>
            </template>

            <n-collapse-transition :show="!editor.collapse">
              <n-form
                  :model="formValue"
                  :rules="formRules"
                  ref="formRefs"
                  :style="{width: editor.formWidth, margin: 'auto'}"
                  :label-placement="editor.placement"
                  label-width="auto"
              >
                <form-grid :editor="editor" :form-value="formValue" :editor-count="row.editorCount"/>
              </n-form>
            </n-collapse-transition>
          </n-card>

          <!-- 选项卡表单 -->
          <n-card
              v-if="row.tabEditors.length > 0"
              class="n-card-tab"
              :style="{width: getCardWidth(row.tabEditors, row.noTabEditors, false)}"
              :bordered="!props.isDrawer"
              :content-style="{paddingBottom: props.isDrawer && 0}"
          >
            <n-tabs type="line" animated>
              <template #suffix>
                <n-button @click="onCollapse(row)" text>
                  {{row.tabEditors[0].collapse ? '展开' : '折叠'}}
                </n-button>
              </template>

              <n-tab-pane :name="editor.name" :tab="editor.title" v-for="editor of row.tabEditors">
                <n-collapse-transition :show="!editor.collapse">
                  <n-form
                      :model="formValue"
                      :rules="formRules"
                      ref="formRefs"
                      :style="{width: editor.formWidth, margin: 'auto'}"
                      :label-placement="editor.placement"
                      label-width="auto"
                  >
                    <form-grid :editor="editor" :form-value="formValue" :editor-count="row.editorCount"/>
                  </n-form>
                </n-collapse-transition>
              </n-tab-pane>
            </n-tabs>
          </n-card>
        </div>

        <!-- 编辑表格 -->
        <div v-for="row of editor.sheeterRows" :class="[props.isDrawer ? 'editor-row-drawer' : 'editor-row']">

          <!-- 非选项卡表格 -->
          <n-card
              :title="sheeter.title"
              v-for="sheeter of row.noTabSheeters"
              :style="{width: getCardWidth(row.tabSheeters, row.noTabSheeters, true, sheeter)}"
              :bordered="!props.isDrawer"
              :content-style="{paddingBottom: props.isDrawer && 0}"
              :segmented="{content: true}"
          >
            <template #header-extra>
              <n-space :size="10">
                <n-button @click="onShowSheetModal(sheeter)" type="primary" text v-if="sheeter.added">
                  添加
                </n-button>

                <n-button @click="onSheeterCollapse(row)" text>
                  {{sheeter.collapse ? '展开' : '折叠'}}
                </n-button>
              </n-space>
            </template>

            <SheeterTable
                :row-index="sheeterRowIndex"
                :sheeter="sheeter"
                @on-double-click="onSheeterDoubleClick"
                @on-update-click="(rowData: any, rowIndex: number) => onSheeterUpdateClick(sheeter, rowData, rowIndex)"
            />
          </n-card>

          <!-- 选项卡表格 -->
          <n-card
              v-if="row.tabSheeters.length > 0"
              :class="['n-card-tab', props.isDrawer ? 'editor-row-drawer' : 'editor-row']"
              :bordered="!props.isDrawer"
              :content-style="{paddingBottom: props.isDrawer && 0}"
              :style="{width: getCardWidth(row.tabSheeters, row.noTabSheeters, true)}"
          >
            <n-tabs type="line" animated @update:value="(value) => onSheeterTabsChange(row, value)">
              <template #suffix>
                <n-space :size="10">
                  <n-button @click="onShowSheetModal(currentTabSheeter)" type="primary" text v-if="currentTabSheeter.added">
                    添加
                  </n-button>

                  <n-button @click="onSheeterCollapse(row)" text>
                    {{row.tabSheeters[0].collapse ? '展开' : '折叠'}}
                  </n-button>
                </n-space>
              </template>

              <n-tab-pane :name="sheeter.name" :tab="sheeter.title" v-for="sheeter of row.tabSheeters">
                <SheeterTable
                    :row-index="sheeterRowIndex"
                    :sheeter="sheeter"
                    @on-double-click="onSheeterDoubleClick"
                    @on-update-click="(rowData: any, rowIndex: number) => onSheeterUpdateClick(sheeter, rowData, rowIndex)"
                />
              </n-tab-pane>
            </n-tabs>
          </n-card>
        </div>

        <!-- 编辑表格弹框 -->
        <n-modal
            v-model:show="showFormModal"
            :title="'添加' + selectSheeter.title"
            :draggable="{bounds: 'none'}"
            :style="{width: '60%'}"
            preset="dialog"
        >
          <n-card style="max-height: 60vh;overflow-y: auto" :bordered="false">
            <n-form
                :model="sheeterRowValue"
                :rules="sheeterFormRules"
                ref="sheeterFormRef"
                label-placement="left"
                label-width="auto"
            >
              <n-grid :cols="24" :x-gap="20">
                <n-form-item-gi
                    :span="8"
                    :label="widget.title"
                    v-for="widget of selectSheeter.widgets"
                    :path="widget.name"
                >
                  <sheeter-widget :widget="widget"/>
                </n-form-item-gi>
              </n-grid>
            </n-form>
          </n-card>

          <template #action>
            <n-button type="primary" @click="onSaveSheeterForm">
              保存
            </n-button>

            <n-button @click="showFormModal = false">
              关闭
            </n-button>
          </template>
        </n-modal>
      </n-scrollbar>
    </div>

    <n-card
        class="form-btn-option"
        :class="isDialog || isDrawer ? '' : 'form-btn-option-page'"
        :style="{width: layoutMainSectionWidth, borderRadius: 0, marginTop: isDialog || isDrawer ? '20px' : ''}"
        :content-style="{display: 'flex', alignItems: 'center', padding: 0}"
        :bordered="!props.isDialog && !props.isDrawer"
    >
      <n-space :size="10">
        <n-button type="primary" @click="handelSave">
          提交
        </n-button>

        <n-button @click="onClose()">
          关闭
        </n-button>
      </n-space>
    </n-card>
  </div>
</template>

<script setup lang="ts">
  import {computed, onMounted, ref, type Ref, type ShallowRef, shallowRef} from "vue";
  /********************************************************************************
   * 通用编辑界面
   *
   * @author Berlin
   ********************************************************************************/
  import type EditorProps from "@/ploutos/graces/ag01/form-editor/EditorProps";
  import {http, loading, message, dialog} from "@/ploutos";
  import WidgetFactories from "@/ploutos/graces/ag01/faces/WidgetFactories.ts";
  import type AbstractWidget from "@/ploutos/graces/ag01/faces/AbstractWidget.ts";
  import useAppStore from "@/ploutos/layouts/store/app-store.ts";
  import useLayoutStore from "@/ploutos/layouts/store/layout-store.ts";
  import {LayoutMode} from "@/ploutos/layouts/types.ts";
  import FormEditorRow from "@/ploutos/graces/ag01/faces/FormEditorRow.ts";
  import type FormEditor from "@/ploutos/graces/ag01/faces/FormEditor.ts";
  import type Sheeter from "@/ploutos/graces/ag01/faces/Sheeter.ts";
  import FormGrid from "@/ploutos/graces/ag01/form-editor/components/FormGrid.vue";
  import SheeterTable from "@/ploutos/graces/ag01/form-editor/components/SheeterTable.vue";
  import type SheeterRow from "@/ploutos/graces/ag01/faces/SheeterRow.ts";

  /**
   * 应用状态
   */
  const appStore = useAppStore();
  const layoutStore = useLayoutStore();

  /**
   * 编辑界面属性定义
   */
  const editor: Ref<EditorProps> = ref({});

  /**
   * 父组件传入的属性
   */
  const props = defineProps({

    /**
     * 模块号
     */
    module: {
      type: String,
      required: true
    },

    /**
     * 界面名称
     */
    name: {
      type: String,
      required: true
    },

    /**
     * 参数
     */
    params: {
      type: Object
    },

    /**
     * 是否查询数据
     */
    fill: {
      type: Boolean,
    },

    /**
     * 是否弹框展示
     */
    isDialog: {
      type: Boolean
    },

    /**
     * 是否抽屉展示
     */
    isDrawer: {
      type: Boolean
    }
  });

  /**
   * 父组件事件
   */
  const emit = defineEmits<{

    /**
     * 保存
     */
    (e: 'onSave', value: any): void;

    /**
     * 关闭弹框(dialog和drawer)
     */
    (e: 'onClose'): void;
  }>();

  /**
   * 表单数据
   */
  const formValue: Ref = ref({});

  /**
   * 表单检验规则
   */
  const formRules: ShallowRef = shallowRef({});

  /**
   * 表单ref
   */
  const formRefs = ref([]);

  /**
   * 组件加载
   */
  onMounted(() => {
    loadDefine();
  });

  /**
   * 加载界面定义
   */
  async function loadDefine() {
    try {
      loading(true);

      const data = {
        module: props.module, name: props.name, params: props.params, local: 'zh-CN'
      }
      const response = await http.post("/ag01/editor/loadDefine", data);
      editor.value = response.data;

      // 计算表单基础属性
      const rules = {}; let tabTitleName;
      editor.value.editorRows.forEach(row => {

        // 选项卡表单
        row.tabEditors = row.editors.filter(i => i.tab);
        row.noTabEditors = row.editors.filter(i => !i.tab);

        // 计算一行展示的表单数
        let editorCount = row.noTabEditors.length;
        editorCount += row.tabEditors > 0 ? 1 : 0;
        row.editorCount = editorCount;

        row.editors.forEach(editor => {
          // 表单校验规则
          editor.widgets.forEach(widget => {
            if (widget.required) {
              rules[widget.name] = {
                required: true,
                message: '请输入' + widget.title,
                trigger: ['blur', 'input'],
              }
            }
            if (widget.tab) {
              tabTitleName = widget.name;
            }
          });
        })
      });
      formRules.value = rules;

      // 拆分编辑表格
      editor.value.sheeterRows.forEach(row => {
        row.tabSheeters = row.sheeters.filter(i => i.tab);
        row.noTabSheeters = row.sheeters.filter(i => !i.tab);
      });
      if (editor.value.sheeterRows.length > 0) {
        currentTabSheeter.value = editor.value.sheeterRows[0].sheeters[0];
      }

      // 加载数据
      if (props.fill) {
        await loadData();
      }

      // 更改选型卡标题
      if (!props.isDialog && !props.isDrawer) {
        if (props.fill) {
          const suffix = tabTitleName ? ' - ' + formValue.value[tabTitleName] : '';
          changeTabTitle(editor.value.etitle, suffix);
        } else {
          changeTabTitle(editor.value.atitle, '');
        }
      }
    } finally {
      loading(false);
    }
  }

  /**
   * 加载表单数据
   */
  async function loadData() {
    try {
      loading(true);

      const data = {
        module: props.module, name: props.name, local: 'zh-CN',
        params: props.params,
      }
      const response = await http.post("/ag01/editor/loadData", data);
      formValue.value = response.data.editor;

      // 编辑表格数据
      editor.value.sheeterRows.forEach(row => {
        row.sheeters.forEach(sheeter => {
          sheeter.data = response.data.sheeter[sheeter.name]
        });
      });
    } finally {
      loading(false);
    }
  }

  /**
   * 更改选项卡标题
   */
  function changeTabTitle(title: string, suffix: string) {
    const interval = setInterval(() => {
      if (appStore.menus.length < 0) {
        return;
      }
      clearInterval(interval);
      appStore.changeTabTitle(title + suffix);
    }, 100);
  }

  /**
   * 保存
   */
  async function handelSave() {
    for (let i = 0; i < formRefs.value.length; i++) {
      await formRefs.value[i].validate((errors) => {
        if (errors) {
          message.error("请将信息填写完整");
        }
      });
    }

    // 编辑表格
    dialog.warning({
      onConfirmClick: () => {
        const data = formValue.value;
        editor.value.sheeterRows.forEach(row => {
          row.sheeters.forEach(sheeter => {
            data[sheeter.name] = sheeter.data;
          });
        });
        emit('onSave', data);
      }
    })
  }

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
      tabEditors: FormEditor[] | Sheeter[], noTabEditors: FormEditor[] | Sheeter[],
      isSheeter: boolean, editor?: FormEditor | Sheeter
  ) {

    // 如果是编辑表格则需要减去gap
    let gap = 0;
    if (isSheeter) {
      let editorCount = noTabEditors.length;
      editorCount += tabEditors.length > 0 ? 1 : 0;
      gap = 10 * (editorCount - 1) / editorCount;
    }

    // 非选项卡，设置了宽度
    if (editor && editor.width) {
      return 'calc(' + editor.width + ' - ' + gap + 'px)';
    }

    // 选项卡，设置了宽度
    if (!editor && tabEditors[0].width) {
      return 'calc(' + tabEditors[0].width + ' - ' + gap + 'px)';
    }

    // 未设置宽度，由100%减去当前行设置了的选项卡宽度
    let restWidth = "100% ", restCount = 0;
    if (tabEditors.length > 0) {
      if (tabEditors[0].width) {
        restWidth += "- " + tabEditors[0].width;
      } else {
        restCount++;
      }
    }

    // 未设置宽度，由100%减去当前行设置了的面板宽度
    // @ts-ignore
    noTabEditors.filter(i => !i.tab).forEach(editor => {
      if (editor.width) {
        restWidth += "- " + editor.width;
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
      return {height: 'calc(100vh - var(--logo-height) - var(--tab-height) - var(--bottom-option-height))'};
    }
    // 抽屉展示
    if (props.isDrawer) {
      return {height: 'calc(100vh - var(--bottom-option-height) - 30px)'};
    }
    // 弹框展示
    if (props.isDialog) {
      return {maxHeight: '60vh', overflowY: 'auto'};
    }
  });

  /**
   * 底部操作按钮宽度
   */
  const layoutMainSectionWidth = computed(() => {
    if (props.isDialog || props.isDrawer) {
      return '100%';
    }
    if (layoutStore.layoutMode == LayoutMode.LeftRight || layoutStore.layoutMode == LayoutMode.TopLeft) {
      if (layoutStore.isCollapse) {
        return 'calc(100% - var(--min-menu-width) - 20px)';
      }
      return 'calc(100% - var(--menu-width) - 20px)';
    }
    if (layoutStore.layoutMode == LayoutMode.LeftSplit) {
      if (layoutStore.isCollapse) {
        return 'calc(100% - var(--tab-split-menu-width) - var(--min-menu-width) - 20px)';
      }
      return 'calc(100% - var(--tab-menu-width) - 20px)';
    }
    if (layoutStore.layoutMode == LayoutMode.TopBottom) {
      return 'calc(100% - 20px)';
    }
  });

  /**
   * 折叠面板
   */
  function onCollapse(row: FormEditorRow) {
    row.editors.forEach(editor => {
      editor.collapse = !editor.collapse
    });
  }

  /********************************************************************************
   * 编辑表格Start
   *
   * @author Berlin
   ********************************************************************************/

  /**
   * 编辑表格弹框
   */
  const showFormModal = ref(false);

  /**
   * 当前操作的编辑表格
   */
  const selectSheeter: Ref<Sheeter> = ref({});

  /**
   * 当前操作编辑表格数据
   */
  const sheeterRowValue: Ref = ref({});

  /**
   * 当前操作编辑表格校验规则
   */
  const sheeterFormRules: ShallowRef = shallowRef({});

  /**
   * 当前操作编辑表格ref
   */
  const sheeterFormRef = ref();

  /**
   * 当前编辑行index
   */
  const sheeterRowIndex = ref(-1);

  /**
   * 选项卡当前选择的表格
   */
  const currentTabSheeter = ref({});

  /**
   * 折叠编辑表格
   */
  function onSheeterCollapse(row: SheeterRow) {
    row.sheeters.forEach(sheeter => {
      sheeter.collapse = !sheeter.collapse;
    });
  }

  /**
   * 编辑表格双击
   */
  function onSheeterDoubleClick(rowIndex: number) {
    sheeterRowIndex.value = rowIndex
  }

  /**
   * 编辑表格修改按钮点击
   */
  function onSheeterUpdateClick(sheeter, rowData: any, rowIndex: number) {
      selectSheeter.value = sheeter;
      sheeterRowIndex.value = rowIndex;
      sheeterRowValue.value = {...rowData};
      showFormModal.value = true;
  }

  /**
   * 编辑表格选项卡切换
   */
  function onSheeterTabsChange(row: SheeterRow, value: string) {
    currentTabSheeter.value = row.sheeters.find(i => i.name == value);
  }

  /**
   * 打开编辑表格弹框
   */
  function onShowSheetModal(sheet: Sheeter) {
    const rules = [];
    sheet.widgets.forEach(widget => {
      if (widget.required) {
        rules[widget.name] = {
          required: true,
          message: '请输入' + widget.title,
          trigger: ['blur', 'input'],
        }
      }
    });
    selectSheeter.value = sheet;
    sheeterRowValue.value = {};
    sheeterRowIndex.value = -1;
    sheeterFormRules.value = rules;
    showFormModal.value = true;
  }

  /**
   * 保存编辑表单值
   */
  async function onSaveSheeterForm() {
    await sheeterFormRef.value.validate((errors) => {
      if (errors) {
        message.error("请将信息填写完整");
      }
    });

    // 修改
    if (sheeterRowIndex.value != -1) {
      selectSheeter.value.data[sheeterRowIndex.value] = sheeterRowValue.value;
      message.success("数据保存成功！可继续修改数据");
    }

    // 添加
    if (sheeterRowIndex.value == -1) {
      let data = selectSheeter.value.data;
      data = !data ? [] : data;

      data.push({...sheeterRowValue.value});
      selectSheeter.value.data = data;
      message.success("数据保存成功！可继续添加数据");
    }
  }

  /**
   * 生成输入控件
   */
  function sheeterWidget(props: {widget: AbstractWidget}) {
    props.widget.rowData = sheeterRowValue.value;
    return WidgetFactories.getInstance().create(props.widget);
  }
</script>

<style scoped lang="scss">
  .editor-row, .editor-row-drawer {
    display: flex;
    gap: 10px;
  }
  .editor-row + .editor-row {
    margin-top: 10px;
  }
  .editor-row-drawer + .editor-row-drawer {
    margin-top: 0;
  }
  .form-btn-option {
    opacity: 0.8;
    align-items: center;
    transition: width $transitionTime;
  }
  .form-btn-option-page {
    height: 100px;
    position: fixed;
    bottom: 0;
  }
</style>