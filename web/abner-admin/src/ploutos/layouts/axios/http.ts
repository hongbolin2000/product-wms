/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import axios, {type AxiosInstance, type AxiosRequestConfig} from 'axios';
/********************************************************************************
 * HTTP请求客户端
 *
 * @author Berlin
 *******************************************************************************/
import {useRequestInterceptor, useResponseInterceptor} from "@/ploutos/layouts/axios/interceptors";

/**
 * HTTP请求类型定义
 */
type AxiosConfig = {

  /**
   * 基础地址
   */
  baseURL: string;

  /**
   * 超时时间（毫秒）缺省30秒
   */
  timeout?: number;
}

namespace http {

  /**
   * token名称
   */
  export const TOKEN_NAME = "authorization";

  /**
   * token前缀
   */
  const tokenPrefix = "Hongyou ";

  /**
   * HTTP示例
   */
  let client: AxiosInstance;

  /**
   * 服务器根地址
   */
  export let basePath = '';

  /**
   * 初始化HTTP配置
   */
  export async function init(config: AxiosConfig) {
    basePath = config.baseURL;

    // 创建axios
    client = axios.create({
      baseURL: basePath,
      timeout: config.timeout ? config.timeout : 30 * 1000,
    });

    // 设置拦截器
    useRequestInterceptor(client);
    useResponseInterceptor(client);
  }

  /**
   * GET请求
   */
  export function get(url: string, config?: AxiosRequestConfig) {
    return client.get(url, config);
  }

  /**
   * POST请求
   */
  export function post(url: string, data?: any, config?: AxiosRequestConfig) {
    return client.post(url, data, config);
  }

  /**
   * 获取token
   */
  export function getToken() {
    return localStorage.getItem(http.TOKEN_NAME);
  }

  /**
   * 设置token
   */
  export function setToken(token: string) {
    localStorage.setItem(http.TOKEN_NAME, tokenPrefix + token);
  }

  /**
   * 移除token
   */
  export function remoteToken() {
    localStorage.removeItem(http.TOKEN_NAME);
  }
}
export default http;
