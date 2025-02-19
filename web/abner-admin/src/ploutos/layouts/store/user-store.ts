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
   * 记住用户名/密码
   */
  username: ref(''),
  password: ref(''),

  /**
   * 记住密码时加密的key
   */
  key: ref(''),

  /**
   * 是否记住账号/密码
   */
  isRememberAccount: ref(false),
  isRememberPassword: ref(false),

  /**
   * 是否自动登录
   */
  isAutoLogin: ref(false),

  /**
   * 用户昵称
   */
  nikeName: ref('')
}
