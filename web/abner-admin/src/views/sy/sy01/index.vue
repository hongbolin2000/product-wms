<template>
  <n-grid :cols="3" :x-gap="10">
    <n-gi>
      <n-card :segmented="{content: true}">
        <template #header>
          <n-select
              label-field="familyLabel"
              value-field="familyName"
              v-model:value="family"
              :options="familyOptions"
              @update:value="loadPermissions"
              style="width: 200px"
          />
        </template>

        <template #header-extra>
          <n-button type="primary">
            提交
          </n-button>
        </template>

        <div style="display: flex; align-items: center;margin-bottom: 10px">
          <n-input v-model:value="pattern" placeholder="输入菜单名称搜索" style="margin-right: 10px"/>
          <n-tooltip trigger="hover">
            <template #trigger>
              <n-switch size="small" @update:value="assignAll"/>
            </template>
            分配全部权限
          </n-tooltip>
        </div>
        <n-tree
            accordion
            key-field="treeId"
            block-line cascade checkable selectable
            :checked-keys="checkedKeys"
            :pattern="pattern"
            :show-irrelevant-nodes="false"
            :data="permissionMenus"
            :node-props="nodeProps"
            @update:checkedKeys="onChecked"
            virtual-scroll
            :style="{maxHeight: 'calc(' + layoutHelper.maxHeight + ' - 154px)'}"
            :override-default-node-click-behavior="override"
        />
      </n-card>
    </n-gi>

    <n-gi :span="2">
      <n-card :segmented="{content: true}" title="权限">
        <template #header-extra>
          <n-button @click="showAllPermissions">
            显示全部
          </n-button>
        </template>
        <n-data-table
            :columns="renderColumns()"
            :data="permissions"
            size="small"
            children-key="none"
            :row-key="rowKey"
            :max-height="'calc(' + layoutHelper.maxHeight + ' - 150px)'"
        />
      </n-card>
    </n-gi>
  </n-grid>
</template>

<script setup lang="ts" name="sy01/assign">
  import {ref, shallowRef, onMounted, type Ref, h} from "vue";
  import {
    DataTableColumn,
    NCheckbox,
    type SelectOption,
    type TreeOption,
    type TreeOverrideNodeClickBehaviorReturn
  } from "naive-ui";
  import type {TableColumn} from "naive-ui/es/data-table/src/interface";
  /********************************************************************************
   * 权限分配
   *
   * @author Berlin
   ********************************************************************************/
  import {http, loading, layoutHelper} from '@/ploutos';
  import type {PermissionMenu} from "@/views/sy/sy01/types.ts";
  import ActionCheck from "@/views/sy/sy01/ActionCheck.vue";

  /**
   * 导航族选项
   */
  const familyOptions: Ref<SelectOption[]> = shallowRef([]);

  /**
   * 选择的导航族
   */
  const family = shallowRef('');

  /**
   * 树形菜单权限
   */
  const permissionMenus = ref<PermissionMenu[]>([]);

  /**
   * 菜单匹配字符
   */
  const pattern = ref('');

  /**
   * 显示在表格中的功能权限
   */
  const permissions = ref<PermissionMenu[]>([]);

  /**
   * 选中的树形key
   */
  const checkedKeys = shallowRef<string[]>([]);

  /**
   * 组件加载
   */
  onMounted( () => {
    loadFamilies();
  });

  /**
   * 加载导航族
   */
  async function loadFamilies() {
    try {
      loading(true);
      const response = await http.get("/sy01/loadFamilies");
      familyOptions.value = response.data.body;
      family.value = response.data.body[0].familyName;

      await loadPermissions();
    } finally {
      loading(false);
    }
  }

  /**
   * 加载导航族菜单权限
   */
  async function loadPermissions() {
    try {
      loading(true);
      const response = await http.get(
          "/sy01/loadPermissions/" + family.value + "/" + navigator.language
      );
      permissionMenus.value = response.data.body;

      calCheckedKeys();
      showAllPermissions();
    } finally {
      loading(false);
    }
  }

  /**
   * 显示全部菜单权限表格数据
   */
  function showAllPermissions() {
    const data = []
    permissionMenus.value.forEach(permission => {
      getPermissions(permission, data);
    });
    permissions.value = data;
  }

  /**
   * 计算已分配的树形key
   */
  function calCheckedKeys() {
    const keys: string[] = []
    getCheckedKeys(permissionMenus.value, keys);
    checkedKeys.value = keys;
  }

  /**
   * 获取已分配的树形key
   */
  function getCheckedKeys(permissionMenus: PermissionMenu[], keys: string[]) {
    for (let permissionMenu of permissionMenus) {
      // 有子节点并且子节点为菜单节点
      if (permissionMenu.children && permissionMenu.children[0].menu) {
        getCheckedKeys(permissionMenu.children, keys);
      }

      // 有子节点并且子节点为权限节点时将菜单权限带入
      if (permissionMenu.children && permissionMenu.children[0].permission) {
        if (!permissionMenu.children[0].permissionActions) {
          permissionMenu.children.forEach(i => i.permissionActions = permissionMenu.permissionActions);
        }
      }
      // 当前菜单所分配的权限
      const assignedActions = permissionMenu.permissionActions.filter(i => i.assigned);
      assignedActions.forEach(action => keys.push(action.permissionCode + '@' + action.actionCode))
    }
  }

  /**
   * 表格列
   */
  function renderColumns(): Array<DataTableColumn> {
    const columns: TableColumn[] = [];
    columns.push({
      key: 'select',
      width: 70,
      title: () => renderAllCheck(),
      render: (rowData: PermissionMenu) => renderRowCheck(rowData)
    });
    columns.push({
      key: 'label',
      width: 120,
      title: '许可名称'
    });
    columns.push({
      key: 'actionName',
      width: '100%',
      title: '权限动作',
      render: (rowData: PermissionMenu) => {
        return h(ActionCheck, {
          permissionActions: rowData.permissionActions,
          calCheckedKeys: () => calCheckedKeys()
        });
      }
    });
    return columns;
  }

  /**
   * 生成表格全选选择框
   */
  function renderAllCheck() {
    return h(NCheckbox, {
      checked: isAllAssigned(), indeterminate: isPartAssigned(),
      onUpdateChecked: (checked: boolean) => {
        permissions.value.forEach(permission => {
          permission.permissionActions.forEach(i => i.assigned = checked);
        });
        calCheckedKeys();
      }
    });
  }

  /**
   * 生成表格行全选选择框
   */
  function renderRowCheck(rowData: PermissionMenu) {
    return h(NCheckbox, {
      checked: rowData.permissionActions.filter(action => !action.assigned).length <= 0,
      indeterminate: isRowPartAssign(rowData),
      onUpdateChecked: (checked: boolean) => {
        rowData.permissionActions.forEach(i => i.assigned = checked);
        calCheckedKeys();
      }
    });
  }

  /**
   * 检查表格数据中是否全部菜单权限都已分配
   */
  function isAllAssigned() {
    let isAllAssign = permissions.value.length > 0;
    for (let permission of permissions.value) {
      // 只有有未分配的权限动作则非权限
      if (permission.permissionActions.filter(action => !action.assigned).length > 0) {
        return false;
      }
    }
    return isAllAssign;
  }

  /**
   * 检查表格数据中是否分配了部分菜单权限
   */
  function isPartAssigned() {
    let hasNoAssign = false, hasAssign = false;
    for (let permission of permissions.value) {
      // 有未分配的权限动作并且也有已分配的权限动，则算部分分配
      if (permission.permissionActions.filter(action => !action.assigned).length > 0) {
        hasNoAssign = true;
      }
      if (permission.permissionActions.filter(action => action.assigned).length > 0) {
        hasAssign = true;
      }
    }
    return hasNoAssign && hasAssign;
  }

  /**
   * 检查当前表格当前行表数据是否分配了部分菜单权限
   */
  function isRowPartAssign(permission: PermissionMenu): boolean {
    const hasNoAssign = permission.permissionActions.filter(action => !action.assigned).length > 0;
    const hasAssign = permission.permissionActions.filter(action => action.assigned).length > 0;
    return hasNoAssign && hasAssign;
  }

  /**
   * 返回表格选择时的Row Key数据
   */
  function rowKey(row: object) {
    return row['treeId'];
  }

  /**
   * 分配全部权限
   */
  function assignAll(checked: boolean) {
    permissionMenus.value.forEach(permission => {
      onChecked([], [], {node: permission, action: checked ? 'check' : 'uncheck'});
    })
  }

  /**
   * 树形菜单勾选
   */
  function onChecked(
      keys: string[], option: PermissionMenu[], meta?: {node: PermissionMenu, action: 'check' | 'uncheck'}
  ) {
    checkedKeys.value = keys;
    treeChecked(meta.node, meta.action == 'check');
    calCheckedKeys();
  }

  /**
   * 树形菜单勾选
   */
  function treeChecked(permissionMenu: PermissionMenu, checked: boolean) {
    if (permissionMenu.children) {
      // 子节点为菜单节点
      if (permissionMenu.children[0].menu) {
        permissionMenu.children.forEach(menu => treeChecked(menu, checked));
      } else {
        permissionMenu.permissionActions.forEach(action => action.assigned = checked);
      }
    }
    // 权限节点
    if (permissionMenu.permission) {
      const action = permissionMenu.permissionActions.find(
          i => permissionMenu.treeId == i.permissionCode + '@' + i.actionCode
      );
      action.assigned = checked;
    }
  }

  /**
   * 属性菜单节点属性
   */
  function nodeProps(info: { option: PermissionMenu }) {
    return {
      onClick() {
        // 当前节点是权限时，点击无效
        if (info.option.permission) {
          return;
        }
        const data = [];
        getPermissions(info.option, data);
        permissions.value = data;
      }
    }
  }

  /**
   * 点击树形菜单选项
   */
  function getPermissions(permissionMenu: PermissionMenu, data: PermissionMenu[]) {
    // 有子节点并且子节点为菜单节点
    if (permissionMenu.children && permissionMenu.children[0].menu) {
      permissionMenu.children.forEach(child => getPermissions(child, data));
    } else {
      data.push(permissionMenu);
    }
  }

  /**
   * 点击节点时的触发效果
   */
  function override(info: {option: TreeOption}): TreeOverrideNodeClickBehaviorReturn {
    if (info.option.children) {
      return 'toggleExpand'
    }
    return 'default'
  }
</script>

<style scoped lang="scss">

</style>