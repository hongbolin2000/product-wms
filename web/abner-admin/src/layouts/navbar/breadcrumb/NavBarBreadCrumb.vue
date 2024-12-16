<template>
  <n-breadcrumb>
    <transition-group name="breadcrumb">
      <n-breadcrumb-item v-for="item of breadcrumbs" :key="item.key">
        <n-dropdown
          v-if="item.children && item.children.length > 0"
          :options="item.children"
          @select="handleSelect"
        >
          <span>
            {{ item.label }}
            <n-icon>
              <ChevronDown />
            </n-icon>
          </span>
        </n-dropdown>
        <span v-else>{{ item.label }}</span>
      </n-breadcrumb-item>
    </transition-group>
  </n-breadcrumb>
</template>

<script setup lang="ts">
  import {type RouteRecordNormalized, useRoute, useRouter} from "vue-router";
  import {onMounted, ref, watch} from "vue";
  import { ChevronDown } from '@vicons/ionicons5'
  /********************************************************************************
   * 导航栏当前菜单面包屑
   *
   * @author Berlin
   ********************************************************************************/

  /**
   * 路由信息
   */
  const route = useRoute()
  const router = useRouter()

  /**
   * 菜单选项
   */
  const breadcrumbs= ref([] as DropItem[] );

  /**
   * 组件加载
   */
  onMounted(() => {
    generatorBreadcrumb();
  })

  /**
   * 计算path的子路径
   */
  function handlePath(path: string): string[] {
    const keys = path.split('/');
    const expireKeys: string[] = keys.filter((item) => !!item);

    return expireKeys.reduce((prev: string, current: string) => {
      const lastItem = prev[prev.length - 1]
      if (!lastItem) {
        prev.push('/' + current)
      } else {
        prev.push(lastItem + '/' + current)
      }
      return prev
    }, [] as string[]);
  }

  /**
   * 生成下拉菜单
   */
  function generatorDropdown(routes: RouteRecordNormalized[], parentPath = '/') {
    if (!routes) {
      return
    }
    const tempArray: DropItem[] = [];
    routes.forEach((it) => {

      // 生成菜单选项
      const tempItem: DropItem = {
        label: it.meta?.title as string,
        key: it.path.startsWith('/') ? it.path : parentPath + '/' + it.path,
        children: [],
      }

      // 生成子菜单
      if (it.children && it.children.length > 0) {
        tempItem.children = generatorDropdown(it.children, tempItem.key);
      } else {
        delete tempItem.children;
      }
      tempArray.push(tempItem);
    })
    return tempArray;
  }

  /**
   * 通过菜单路径查询路由
   */
  function findRoute(paths: string[]) {
    const selectRoutes: RouteRecordNormalized[] = [];
    let routes: RouteRecordNormalized[] = router.getRoutes();

    paths.forEach((path) => {
      const selectRoute = routes.find((item) => item.path === path);
      if (selectRoute) {
        routes = selectRoute.children;
        selectRoutes.push(selectRoute);
      }
    })
    return selectRoutes;
  }

  /**
   * 生成当前菜单的面包屑
   */
  function generatorBreadcrumb() {
    breadcrumbs.value = [];
    const paths = handlePath(route.path);
    const findedRoutes = findRoute(paths);

    const items = generatorDropdown(findedRoutes);
    if (items) {
      breadcrumbs.value.push(...items)
    }
  }

  /**
   * 点击下拉菜单
   */
  function handleSelect(key: string) {
    router.push(key)
  }

  /**
   * 侦听菜单变化
   */
  watch(() => route.path, () => {
    if (route.path.startsWith('/redirect') || ['/login', '/404', '/405', '/403', '/500'].includes(route.path)) {
      return;
    }
    generatorBreadcrumb();
  })

  /**
   * 菜单类型定氮仪
   */
  interface DropItem {

    /**
     * 菜单标签
     */
    label: string;

    /**
     * 菜单的key
     */
    key: string

    /**
     * 子菜单
     */
    children?: DropItem[];
  }
</script>
