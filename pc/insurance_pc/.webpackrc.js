const path = require('path');

export default {
  entry: 'src/index.js',
  extraBabelPlugins: [
    ['import', {
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
  publicPath: 'https://www.freelycar.com/carInsurance/',
  // publicPath: '/',
  disableDynamicImport: true,
  hash: true,
  proxy: {
    '/api': {
      target: "https://www.freelycar.com/carInsurance/api",
      // target: "http://localhost:8080/carInsurance/api",
      changeOrigin: true,
      pathRewrite: {
        "^/api": ""
      }
    }
  }
};
