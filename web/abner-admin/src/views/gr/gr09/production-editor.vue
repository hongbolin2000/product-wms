<template>
  <Graces.FormEditor module="gr09" name="productionEdit" @on-loaded="handleLoaded" @on-save="handleSave"/>
</template>

<script setup lang="ts">
  import {ref} from "vue";
  /********************************************************************************
   * 生产录入
   *
   * @author Berlin
   ********************************************************************************/
  import {Graces, http, loading, message, useGracesStore} from '@/ploutos';
  import type {
    FormEditorProps, FormEditorRowProps, TextWidgetProps, EditorProps, SuggestorWidgetProps, TextWidgetRefProps
  } from '@/ploutos/types';

  /**
   * 通用界面应用全局状态
   */
  const gracesStore = useGracesStore();

  /**
   * 成品关联物料清单
   */
  const materials = ref([]);

  /**
   * 原料序列号输入框Ref
   */
  let inputRefs: TextWidgetRefProps[] = [];

  /**
   * 界面定义加载完成
   */
  function handleLoaded() {
    // 获取表单定义
    const editor = gracesStore.getEditor('gr09', 'productionEdit');
    const productionInfoEditor = editor.allEditors.find(i => i.name == 'production.info');

    // 设置成品物料查询建议器值更改回调函数
    const widget: SuggestorWidgetProps = <SuggestorWidgetProps> productionInfoEditor.widgets.find(
        i => i.name == 'productMaterialId'
    );
    widget.onValueUpdate = (value: string) => getRawMaterials(editor, value);
  }

  /**
   * 查询成品关联原料
   */
  async function getRawMaterials(editor: EditorProps, value: string) {
    if (value == null) {
      return;
    }

    try {
      loading(true);
      const response = await http.get("/gr09/getRawMaterials/" + value);
      materials.value = response.data.body;
      inputRefs = [];

      // 移除原有的表单
      editor.editorRows.splice(1, editor.editorRows.length - 1);

      const widgets: TextWidgetProps[] = <TextWidgetProps[]>[];
      for (let i = 0; i < materials.value.length; i++) {
        const material: any = materials.value[i];

        // 输入控件
        widgets.push(<TextWidgetProps>{
          type: 'text', name: material.materialId + '-rawSerialNo', title: material.skuName, spans: 1, maxLength: 64,
          inputRefs: inputRefs, onEnter: handleEnter
        });
      }

      // 表单
      let formEditors: FormEditorProps[] = [];
      formEditors.push(<FormEditorProps>{
        name: 'productionLine', title: '生产原料', spans: 4, widgets: <TextWidgetProps[]> widgets, placement: 'left'
      });

      // 生成表单行
      const formEditorRow: FormEditorRowProps = {
        editors: formEditors, editorCount: 1, noTabEditors: formEditors, tabEditors: [],
      };
      editor.editorRows.push(formEditorRow);
    } finally {
      loading(false);
    }
  }

  /**
   * 原料序列号输入框回车事件
   */
  function handleEnter(name: string) {
    let index = inputRefs.findIndex(i => i.name == name) + 1;
    index = index == inputRefs.length ? 0 : index;
    inputRefs[index].el.select();
  }

  /**
   * 保存
   */
  async function handleSave(value: any) {
    try {
      const productionLines = [];

      for (let material of materials.value) {
        const rawSerialNo = value[material.materialId + '-rawSerialNo'];

        if (!rawSerialNo) {
          continue;
        }

        productionLines.push({
          rawSerialNo: rawSerialNo,
          materialId: material.materialId
        });
      }
      value.productionLines = productionLines;

      loading(true);
      await http.post("/gr09/save", value);
      message.success('生产序列号[ ' + value.fgSerialNo + ' ]录入成功');

      // 刷新列表
      const func = new Function( 'name', 'return eval(name).call()');
      func('gr09ProductionListRefresh');
    } finally {
      loading(false);
    }
  }
</script>

<style scoped lang="scss">

</style>