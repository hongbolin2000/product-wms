/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {defineStore} from "pinia";
import {ref} from "vue";
/********************************************************************************
 * 用户全局应用状态
 *
 * @author Berlin
 ********************************************************************************/

/**
 * 创建用户全局应用状态
 */
const useUserStore = defineStore('userStore', () => {
  return { ...userStore }
}, {
  persist: true
});
export default useUserStore;

/**
 * 初始用户全局应用状态
 */
const userStore = {

  /**
   * 记住用户名
   */
  username: ref(''),

  /**
   * 是否记住账号
   */
  isRememberAccount: ref(false),

  /**
   * 是否自动登录
   */
  isAutoLogin: ref(false),
}
