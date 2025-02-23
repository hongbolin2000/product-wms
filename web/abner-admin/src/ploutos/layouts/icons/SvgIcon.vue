<template>
  <component :is="component" :class="className" aria-hidden="true">
    <use :href="iconName" />
  </component>
</template>

<script setup lang="ts">
  import {computed} from "vue";
  /********************************************************************************
   * 图标展示组件
   *
   * @author Berlin
   ********************************************************************************/

  /**
   * 父组件传入的属性
   */
  const props = defineProps({

    /**
     * 图标类型（svg/icon）
     */
    type: {
      type: String,
      default: 'icon',
    },

    /**
     * 图标名称
     */
    name: {
      type: String,
      required: true
    }
  });

  /**
   * 展示的原生组件
   */
  const component = computed(() => {
    return props.type === 'svg' ? 'svg' : 'i';
  });

  /**
   * 样式类名
   */
  const className = computed(() => {
    // svg
    if (props.type === 'svg') {
      return 'svg-icon'
    }
    // ali iconfont
    if (props.type === 'icon') {
      return 'iconfont icon-' + props.name
    }
    return '';
  });

  /**
   * 展示的图标名
   */
  const iconName = computed(() => {
    return `#${props.type == 'svg' ? 'icon' : 'iconfont'}-${props.name}`
  });
</script>

<style scoped lang="scss">
  .svg-icon {
    width: 1em;
    height: 1em;
    vertical-align: -0.15em;
    fill: currentColor;
    overflow: hidden;
  }
  .svg-icon:hover {
    fill: var(--primary-color-hover);
  }

  .svg-external-icon {
    background-color: currentColor;
    mask-size: cover !important;
    display: inline-block;
  }
</style>
