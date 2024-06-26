import ComponentsPlugin from 'unplugin-vue-components/webpack'
import NutUIResolver from '@nutui/nutui-taro/dist/resolver';
const webpack = require('webpack');

const path = require('path');

const config = {
    projectName: '云职评',
    date: '2023-3-10',
    designWidth(input) {
        if (input?.file?.replace(/\\+/g, '/').indexOf('@nutui') > -1) {
            return 375
        }
        return 750
    },
    deviceRatio: {
        640: 2.34 / 2,
        750: 1,
        828: 1.81 / 2,
        375: 2 / 1
    },
    sourceRoot: 'src',
    outputRoot: 'dist',
    plugins: ['@tarojs/plugin-html'],
    defineConstants: {},
    copy: {
        patterns: [],
        options: {}
    },
    framework: 'vue3',
    compiler: {
        type: 'webpack5',
        prebundle: {enable: false}
    },
    cache: {
        enable: false // Webpack 持久化缓存配置，建议开启。默认配置请参考：https://docs.taro.zone/docs/config-detail#cache
    },
    sass: {
        resource: [
            path.resolve(__dirname, '..', 'src/custom_theme.scss')
        ],
        data: `@import "@nutui/nutui-taro/dist/styles/variables.scss";`
    },
    h5: {
        webpackChain(chain) {
            chain.plugin('unplugin-vue-components').use(ComponentsPlugin({
                resolvers: [NutUIResolver({taro: true})]
            }))
        },
        publicPath: process.env.NODE_ENV === 'production' ? '/mobile' : '/',
        staticDirectory: 'static',
        esnextModules: ['nutui-taro', 'icons-vue-taro'],
        postcss: {
            autoprefixer: {
                enable: true,
                config: {}
            },
            cssModules: {
                enable: false, // 默认为 false，如需使用 css modules 功能，则设为 true
                config: {
                    namingPattern: 'module', // 转换模式，取值为 global/module
                    generateScopedName: '[name]__[local]___[hash:base64:5]'
                }
            }
        },
        devServer: {
            proxy: {
                '/sg-user-service/': {
                    target: 'http://127.0.0.1:8080/',
                    changeOrigin: true
                }
            }
        }
    }
}

module.exports = function (merge) {
    if (process.env.NODE_ENV === 'development') {
        return merge({}, config, require('./dev'))
    }
    return merge({}, config, require('./prod'))
}
