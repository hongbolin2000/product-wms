/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
/********************************************************************************
 * HTTP请求拦截器
 *
 * @author Berlin
 *******************************************************************************/
import type {AxiosError, AxiosInstance, AxiosResponse, InternalAxiosRequestConfig} from "axios";
import {notification, dialog} from "@/ploutos";
import {TOKEN_NAME} from '@/ploutos/layouts/types';
import {useRouter} from "vue-router";

/**
 * token前缀
 */
const tokenPrefix = "Hongyou ";

/**
 * 请求式拦截下
 */
export function useRequestInterceptor(client: AxiosInstance) {
  client.interceptors.request.use((config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem(TOKEN_NAME)
    if (token) {
      config.headers[TOKEN_NAME] = tokenPrefix + token;
    }
    return config;
  });
}

/**
 * 响应式拦截器
 */
export function useResponseInterceptor(client: AxiosInstance) {
  const router = useRouter();
  client.interceptors.response.use((response: AxiosResponse) => {

    // 检查是否有code字段，不为200则提示报错
    if (response.data.code && response.data.code != 200) {
      notification.warning(response.data.message);
      return Promise.reject(response);
    }
    return response;
  }, (error: AxiosError) => {

    // 服务器无法连接
    if (error.code == "ERR_NETWORK") {
      notification.error('服务器无响应');
      return Promise.reject(error);
    }

    // 服务器请求超时
    if (error.code == "ECONNABORTED") {
      notification.error('服务器请求超时');
      return Promise.reject(error);
    }

    // 无响应结果
    if (!error.response) {
      return Promise.reject(error);
    }

    // 401
    if (error.response.status == 401) {
      dialog.warning({
        closable: false,
        content: '用户认证已失效，需重新登录！',
        noCancel: true,
        onConfirmClick: async () => {
          await router.replace("/login");
        }
      });
      return Promise.reject(error);
    }

    // 404
    if (error.response.status == 404) {
      notification.error('请求路径不正确');
      return Promise.reject(error);
    }

    // 500
    if (error.response.status === 500) {
      if (error.response.data) {
        notification.error(error.response.data.toString());
      } else {
        notification.error('服务器出错了');
      }
      return Promise.reject(error);
    }
    return Promise.reject(error);
  });
}
