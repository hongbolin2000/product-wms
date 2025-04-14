/*
 * Copyright 2024, Hongyou Software Development Studio.
 */
import { fileURLToPath, URL } from 'node:url'
import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import vueDevTools from 'vite-plugin-vue-devtools'
import ViteComponents from 'unplugin-vue-components/vite'
import { NaiveUiResolver } from 'unplugin-vue-components/resolvers'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import path from "path";
/********************************************************************************
 * Vite配置文件 https://vite.dev/config/
 *
 * @author Berlin
 ********************************************************************************/
export default defineConfig(({ mode }: any) => {

  /**
   * 环境变量
   */
  const env: Record<string, string> = loadEnv(mode, process.cwd());

  return {
    base: env.VITE_BASE_URL,
    plugins: [
      vue(),
      vueJsx(),
      vueDevTools(),
      ViteComponents({
        resolvers: [NaiveUiResolver()]
      }),
      createSvgIconsPlugin({
        iconDirs: [path.resolve(process.cwd(), '@/icons')],
        symbolId: 'icon-[dir]-[name]',
      }),
    ],
    css: {
      preprocessorOptions: {
        scss: {
          additionalData: '@use "@/ploutos/layouts/styles/variables.scss" as *;',
          api: 'modern'
        },
      },
    },
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      },
    },
    server: {
      port: 9090,
      proxy: {
        [env.VITE_API_BASE_URL]: {
          target: env.VITE_API_SERVER_URL,
          changeOrigin: true,
          rewrite: (path) => path.replace(env.VITE_API_BASE_URL, ''),
        },
      },
    }
  }
})
