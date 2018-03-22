module.exports = [{
    code: 1,
    name: "车辆损失险",
    isToubao: true,    //是否投保
    compensation: true, //不计免赔
    amountStr: '',  //   可以是国产/进口  100万/50万   string类型
    selectAry:[],
    selectIndex: 0,
    unit: '' //单位
},{
    code: 2,
    name: "第三者责任险",
    isToubao: false,    //是否投保
    compensation: false, //不计免赔
    amountStr: '100万',  //   可以是国产/进口  100万/50万   string类型
    selectAry:['200万', '150万', '100万', '50万', '30万', '20万', '10万', '5万'],
    tureAry:[2000000, 1500000, 1000000, 500000, 300000, 200000, 100000, 50000],
    selectIndex: 2,
    unit: '' //单位
},{
    code: 3,
    name: "全车盗抢险",
    isToubao: false,    //是否投保
    compensation: false, //不计免赔
    amountStr: '',  //   可以是国产/进口  100万/50万   string类型
    selectAry:[],
    selectIndex: 0,
    unit: '' //单位
},{
    code: 4,
    name: "司机座位责任险",
    isToubao: false,    //是否投保
    compensation: false, //不计免赔
    amountStr: '10万',  //   可以是国产/进口  100万/50万   string类型
    selectAry:['200万', '150万', '100万', '50万', '30万', '20万', '10万', '5万'],
    tureAry:[100000, 90000, 80000, 70000, 60000, 50000, 40000, 30000, 20000, 10000],
    selectIndex: 6,
    unit: '/座' //单位
},{
    code: 5,
    name: "乘客座位责任险",
    isToubao: false,    //是否投保
    compensation: false, //不计免赔
    amountStr: '10万',  //   可以是国产/进口  100万/50万   string类型
    selectAry:['200万', '150万', '100万', '50万', '30万', '20万', '10万', '5万'],
    tureAry:[100000, 90000, 80000, 70000, 60000, 50000, 40000, 30000, 20000, 10000],
    selectIndex: 6,
    unit: '/座' //单位
},{
    code: 6,
    name: "玻璃破碎险",  //玻璃单独破碎险
    isToubao: false,    //是否投保
    compensation: false, //不计免赔
    amountStr: '国产',  //   可以是国产/进口  100万/50万   string类型
    selectAry:['国产', '进口'],
    tureAry:['国产', '进口'],
    selectIndex: 0,
    unit: '' //单位
},{
    code: 7,
    name: "车身划痕损失险",
    isToubao: false,    //是否投保
    compensation: false, //不计免赔
    amountStr: '2万',  //   可以是国产/进口  100万/50万   string类型
    selectAry:['2万', '1万', '5000', '2000'],
    tureAry:[20000, 10000, 5000, 2000],
    selectIndex: 0,
    unit: '' //单位
},{
    code: 8,
    name: "自燃损失险",
    isToubao: false,    //是否投保
    compensation: false, //不计免赔
    amountStr: '',  //   可以是国产/进口  100万/50万   string类型
    selectAry:[],
    selectIndex: 0,
    unit: '' //单位
},{
    code: 9,
    name: "发动机涉水损失险",
    isToubao: false,    //是否投保
    compensation: false, //不计免赔
    amountStr: '',  //   可以是国产/进口  100万/50万   string类型
    selectAry:[],
    selectIndex: 0,
    unit: '' //单位
},{
    code: 10,
    name: "指定专修厂特约条款",
    isToubao: false,    //是否投保
    compensation: false, //不计免赔
    amountStr: '',  //   可以是国产/进口  100万/50万   string类型
    selectAry:[],
    selectIndex: 0,
    unit: '' //单位
},{
    code: 11,
    name: "无法找到第三方特约险",
    isToubao: false,    //是否投保
    compensation: false, //不计免赔
    amountStr: '',  //   可以是国产/进口  100万/50万   string类型
    selectAry:[],
    selectIndex: 0,
    unit: '' //单位
},
]