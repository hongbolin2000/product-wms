/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import {defineStore} from "pinia";
import {shallowRef} from "vue";
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
  username: shallowRef(''),
  password: shallowRef(''),

  /**
   * 记住密码时加密的key
   */
  key: shallowRef(''),

  /**
   * 是否记住账号/密码
   */
  isRememberAccount: shallowRef(false),
  isRememberPassword: shallowRef(false),

  /**
   * 是否自动登录
   */
  isAutoLogin: shallowRef(false),

  /**
   * 用户昵称
   */
  nikeName: shallowRef(''),

  /**
   * 用户头像
   */
  avatar: shallowRef('')
}
