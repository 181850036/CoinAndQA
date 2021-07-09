const path=require('path')
const CompressionPlugin =require('compression-webpack-plugin')
const productionGzipExtensions = ['js', 'css']

module.exports = {
    transpileDependencies: [
        'vuetify'
    ],
    publicPath: '/',
    devServer: {
        proxy: {
            '/api/question':{
                target: 'http://81.68.74.201:5000', //目标接口地址，设置调用的接口域名和端口号 别忘了加http、https
                changeOrigin: true, //是否跨域
                secure: true, // 允许https请求
                pathRewrite: {
                    '^/api': '' // 路径重写 将 target 中的目标接口地址重写为 /api
                },
                onProxyReq: (proxyReq, req, res) => {
                    if (proxyReq.getHeader('origin'))
                        proxyReq.setHeader('origin', 'http://81.68.74.201:5000')
                }
            },
            '/api/getAnswer':{
                target: 'http://139.9.237.6:5000', //目标接口地址，设置调用的接口域名和端口号 别忘了加http、https
                changeOrigin: true, //是否跨域
                secure: true, // 允许https请求
                pathRewrite: {
                    '^/api': '' // 路径重写 将 target 中的目标接口地址重写为 /api
                },
                onProxyReq: (proxyReq, req, res) => {
                    if (proxyReq.getHeader('origin'))
                        proxyReq.setHeader('origin', 'http://139.9.237.6:5000')
                }
            },
            '/api': {  //匹配接口路径中的 /api
                target: 'http://121.5.8.40:8001', //目标接口地址，设置调用的接口域名和端口号 别忘了加http、https
                changeOrigin: true, //是否跨域
                secure: true, // 允许https请求
                pathRewrite: {
                    '^/api': '' // 路径重写 将 target 中的目标接口地址重写为 /api
                },
                onProxyReq: (proxyReq, req, res) => {
                    if (proxyReq.getHeader('origin'))
                        proxyReq.setHeader('origin', 'http://121.5.8.40:8001')
                }
            }
        }
    },
    configureWebpack: {
        plugins:[
            new CompressionPlugin({
                algorithm:'gzip',
                test:new RegExp('\\.('+productionGzipExtensions.join('|')+')$'),
                threshold:10240,
                deleteOriginalAssets: false, // 不删除源文件
                minRatio:0.8
            }),
        ],
        resolve: {
            alias: {
                "@ant-design/icons/lib/dist$": path.resolve(__dirname, "./src/icons.js")
            }
        }
    }
}
