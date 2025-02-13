<template>
  <n-card
    :content-style="{ padding: '0px' }"
    :footer-style="{ padding: '0px' }"
    :bordered="false"
    :segmented="true"
  >
    <div v-if="messages.length > 0">
      <div v-for="(item, index) of messages" :key="index"
           style="display: flex;align-items: center;padding: 0.25rem"
      >
        <div>
          <n-icon size="40" color="#f00">
            <NotificationsCircle />
          </n-icon>
        </div>
        <div style="margin-left: 0.5rem;">
          <div style="line-height: 1.25rem">{{ item.title }}</div>
          <n-ellipsis :line-clamp="1" style="color: rgb(107 114 128 / 1)">{{ item.desc }}</n-ellipsis>
        </div>
      </div>
    </div>
    <n-empty v-else description="暂无消息哦~" style="padding: 5rem 5rem"/>
    <template #footer>
      <div class="flex justify-evenly" style="display: flex; justify-content: space-evenly">
        <n-button type="text" @click="onClearMessage">清空消息</n-button>
        <n-button type="text">查看更多</n-button>
      </div>
    </template>
  </n-card>
</template>

<script setup lang="ts">
  import {ref} from "vue";
  import { NotificationsCircle } from '@vicons/ionicons5'
  /********************************************************************************
   * 导航栏通知消息
   *
   * @author Berlin
   ********************************************************************************/

  /**
   * 事件
   */
  const emit = defineEmits(['clear']);

  /**
   * 消息列表
   */
  const messages = ref([
    {
      title: '您收到一个消息',
      desc: '来自好友张三的生日祝福',
    },
    {
      title: '您收到一个消息',
      desc: '来自好友张三的生日祝福来自好友张三的生日祝福',
    },
    {
      title: '您收到一个消息',
      desc: '来自好友张三的生日祝福来自好友张三的生日祝福',
    },
    {
      title: '您收到一个消息',
      desc: '来自好友张三的生日祝福来自好友张三的生日祝福来自好友张三的生日祝福来自好友张三的生日祝福',
    },
    {
      title: '您收到一个消息',
      desc: '来自好友张三的生日祝福',
    },
    {
      title: '您收到一个消息',
      desc: '来自好友张三的生日祝福',
    },
  ]);

  /**
   * 情况消息
   */
  function onClearMessage() {
    messages.value.length = 0
    emit('clear')
  }
</script>
