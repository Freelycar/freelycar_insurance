const path = require('path');

export default {
  entry: 'src/index.js',
  extraBabelPlugins: [
    'transform-decorators-legacy', ['import', {
      libraryName: 'antd',
      libraryDirectory: 'es',
      style: true
    }],
  ],
  env: {
    development: {
      extraBabelPlugins: ['dva-hmr']
    },
  },
  alias: {
    'components': path.resolve(__dirname, 'src/components/'),
  },
  ignoreMomentLocale: true,
  theme: './src/theme.js',
  html: {
    template: './src/index.ejs',
  },
  publicPath: 'https://www.howmuchweb.com/carInsurance/',
  disableDynamicImport: true,
  hash: true,
  proxy: {
    '/api': {
      target: "http://172.17.3.105:8080/carInsurance/api",
      changeOrigin: true,
      pathRewrite: {
        "^/api": ""
      }
    }
  }
};
