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
  export function post(url: string, data?: any) {
    return client.post(url, data);
  }
}
export default http;
