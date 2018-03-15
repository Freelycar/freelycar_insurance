import { isUrl } from '../utils/utils';

const menuData = [{
  name: '车险客户',
  icon: 'user',
  path: 'client',
  children: [{
    // name: '客户列表',
    path: 'list',
  },
  {
    // name: '客户详情',
    path: 'detail',
    // hideInMenu: true,  // 隐藏该条
  }],
}, {
  name: '业绩统计',
  icon: 'area-chart',
  path: 'performance',
  children: [{
    // name: '业绩统计',
    path: 'statistics',
  }],
},{
  name: '渠道管理',
  icon: 'fork',
  path: 'channel',
  children: [{
    // name: '基础详情页',
    path: 'manage',
  }],
},{
  name: '账户',
  icon: 'user',
  path: 'user',
  authority: 'guest',
  children: [{
    name: '登录',
    path: 'login',
  }, {
    name: '注册',
    path: 'register',
  }, {
    name: '注册结果',
    path: 'register-result',
  }],
}]
function formatter(data, parentPath = '/', parentAuthority) {
  return data.map((item) => {
    let { path } = item;
    if (!isUrl(path)) {
      path = parentPath + item.path;
    }
    const result = {
      ...item,
      path,
      authority: item.authority || parentAuthority,
    };
    if (item.children) {
      result.children = formatter(item.children, `${parentPath}${item.path}/`, item.authority);
    }
    return result;
  });
}

export const getMenuData = () => formatter(menuData);
