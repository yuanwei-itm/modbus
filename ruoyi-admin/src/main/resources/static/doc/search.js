let api = [];
api.push({
    alias: 'api',
    order: '1',
    desc: '在线用户监控',
    link: '在线用户监控',
    list: []
})
api[0].list.push({
    order: '1',
    methodId: 'cfb96cf514b931116ef1657c93ffc08e',
    desc: 'list',
});
api[0].list.push({
    order: '2',
    methodId: '061745c79aa1533ec2f0ec99957c1966',
    desc: '强退用户',
});
api.push({
    alias: 'SysJobController',
    order: '2',
    desc: '调度任务信息操作处理',
    link: '调度任务信息操作处理',
    list: []
})
api[1].list.push({
    order: '1',
    methodId: '5d9d130f064dd2118a01c95081ec23fc',
    desc: '查询定时任务列表',
});
api[1].list.push({
    order: '2',
    methodId: '314b7fd5a68517745496c5c8d5133862',
    desc: '导出定时任务列表',
});
api[1].list.push({
    order: '3',
    methodId: '82bb9cb1c3b154b829c6ee6fe50ca729',
    desc: '获取定时任务详细信息',
});
api[1].list.push({
    order: '4',
    methodId: '4bdc6d0ccda0e07525ceb89add9f7de2',
    desc: '新增定时任务',
});
api[1].list.push({
    order: '5',
    methodId: 'd932e6745c8702c64139f085c8424e0c',
    desc: '修改定时任务',
});
api[1].list.push({
    order: '6',
    methodId: '19fa1495657a20f5dcfbb19a66f0b8bb',
    desc: '定时任务状态修改',
});
api[1].list.push({
    order: '7',
    methodId: '5966bfe7afc8a104b590e812c01381e2',
    desc: '定时任务立即执行一次',
});
api[1].list.push({
    order: '8',
    methodId: '4f0d01c84f301d61a80eef058170e445',
    desc: '删除定时任务',
});
api.push({
    alias: 'CacheController',
    order: '3',
    desc: '缓存监控',
    link: '缓存监控',
    list: []
})
api[2].list.push({
    order: '1',
    methodId: '13c75647d1930e05da4ae36b5d270584',
    desc: 'getInfo',
});
api[2].list.push({
    order: '2',
    methodId: '1338f1c844f3ebe759b2de34bc63a670',
    desc: 'cache',
});
api[2].list.push({
    order: '3',
    methodId: '1d9ed3f7fa6e073815cbe226a34f83a2',
    desc: 'getCacheKeys',
});
api[2].list.push({
    order: '4',
    methodId: '44733726112361f6f8642378902e8ed8',
    desc: 'getCacheValue',
});
api[2].list.push({
    order: '5',
    methodId: 'e42eba51d7a6f986cfcac55a93d5d825',
    desc: 'clearCacheName',
});
api[2].list.push({
    order: '6',
    methodId: '9a878708561a33a3131fe6073208ed01',
    desc: 'clearCacheKey',
});
api[2].list.push({
    order: '7',
    methodId: '2ba94e41cbe47c0deb3971147143302f',
    desc: 'clearCacheAll',
});
api.push({
    alias: 'SysRegisterController',
    order: '4',
    desc: '注册验证',
    link: '注册验证',
    list: []
})
api[3].list.push({
    order: '1',
    methodId: '2f448cffcc32aa183a49c507b1f0e3d3',
    desc: 'register',
});
api.push({
    alias: 'SysIndexController',
    order: '5',
    desc: '首页',
    link: '首页',
    list: []
})
api[4].list.push({
    order: '1',
    methodId: 'af26e1b5a1923fde12e54a26d4d0aa73',
    desc: '访问首页，提示语',
});
api.push({
    alias: 'SysProfileController',
    order: '6',
    desc: '个人信息 业务处理',
    link: '个人信息_业务处理',
    list: []
})
api[5].list.push({
    order: '1',
    methodId: '3737a6438cd7bcb6ea3319fbbc6bff51',
    desc: '个人信息',
});
api[5].list.push({
    order: '2',
    methodId: '87398a1cb65058a789e02b30e6c45de8',
    desc: '修改用户',
});
api[5].list.push({
    order: '3',
    methodId: 'b2faea3353bb496ac2a8b4325808a958',
    desc: '重置密码',
});
api[5].list.push({
    order: '4',
    methodId: '54038396af86f3ea018d2825ad8f57eb',
    desc: '头像上传',
});
api.push({
    alias: 'SysRoleController',
    order: '7',
    desc: '角色信息',
    link: '角色信息',
    list: []
})
api[6].list.push({
    order: '1',
    methodId: '9fbfbd556d6c728ecdc99ff1ec614782',
    desc: 'list',
});
api[6].list.push({
    order: '2',
    methodId: '9b14b9e6c04cc3d5e8dec874cd92fec1',
    desc: 'export',
});
api[6].list.push({
    order: '3',
    methodId: 'f43f4f80928be68ac6f0b95d677ff78f',
    desc: '根据角色编号获取详细信息',
});
api[6].list.push({
    order: '4',
    methodId: '52893d0a50cf760b54a4c186bb078106',
    desc: '新增角色',
});
api[6].list.push({
    order: '5',
    methodId: 'e2d7bbeee55d702e8bbca3d2c90ac413',
    desc: '修改保存角色',
});
api[6].list.push({
    order: '6',
    methodId: '64cd8ea7b564dd15f157c5b5ac3bd4f9',
    desc: '修改保存数据权限',
});
api[6].list.push({
    order: '7',
    methodId: '08704b895f9828be4331b6890110cb15',
    desc: '状态修改',
});
api[6].list.push({
    order: '8',
    methodId: '9af7ee1b5bdc7efeafe4887658de5051',
    desc: '删除角色',
});
api[6].list.push({
    order: '9',
    methodId: '580f1cfed443cc5b42b2f01f6c911b27',
    desc: '获取角色选择框列表',
});
api[6].list.push({
    order: '10',
    methodId: '8474f286272df9a21a03ff7d761e500c',
    desc: '查询已分配用户角色列表',
});
api[6].list.push({
    order: '11',
    methodId: '5adc8ce3d33dcd9c9a2c6bf6d5ae6ec1',
    desc: '查询未分配用户角色列表',
});
api[6].list.push({
    order: '12',
    methodId: '0e3de9d1705243b2038e1d0fee408068',
    desc: '取消授权用户',
});
api[6].list.push({
    order: '13',
    methodId: 'b0d811fe9b8b1f52f7304d09aaacb227',
    desc: '批量取消授权用户',
});
api[6].list.push({
    order: '14',
    methodId: 'a36da3054507509268558b5d2e7d568a',
    desc: '批量选择用户授权',
});
api[6].list.push({
    order: '15',
    methodId: 'b1ceab397dd0e2798a0f1821c6dc3616',
    desc: '获取对应角色部门树列表',
});
api.push({
    alias: 'SysMenuController',
    order: '8',
    desc: '菜单信息',
    link: '菜单信息',
    list: []
})
api[7].list.push({
    order: '1',
    methodId: '71455ddffacd22eae7da38f2e1b79655',
    desc: '获取菜单列表',
});
api[7].list.push({
    order: '2',
    methodId: 'dcff25607def93e83259ed54df87eaf8',
    desc: '根据菜单编号获取详细信息',
});
api[7].list.push({
    order: '3',
    methodId: '9f5e13027efe579299e40688ca30e3c2',
    desc: '获取菜单下拉树列表',
});
api[7].list.push({
    order: '4',
    methodId: '922d9a4fdf87ed37449248ad300ce147',
    desc: '加载对应角色菜单列表树',
});
api[7].list.push({
    order: '5',
    methodId: '35ed148197b7ef35b69e5578dbb72c3c',
    desc: '新增菜单',
});
api[7].list.push({
    order: '6',
    methodId: 'fa3b954c35b97540f5e2af3b97dffd2d',
    desc: '修改菜单',
});
api[7].list.push({
    order: '7',
    methodId: 'e4ad6d38757ce629dbbcf3ace4fa9df7',
    desc: '删除菜单',
});
api.push({
    alias: 'TestController',
    order: '9',
    desc: 'swagger 用户测试方法',
    link: 'swagger_用户测试方法',
    list: []
})
api[8].list.push({
    order: '1',
    methodId: '53b63c9411eccc51a1421178132d6857',
    desc: 'userList',
});
api[8].list.push({
    order: '2',
    methodId: 'd6e3a7b54d727e4ed033d90863730f1a',
    desc: 'getUser',
});
api[8].list.push({
    order: '3',
    methodId: 'dea943194ec8e43f216bea8da9f4de70',
    desc: 'save',
});
api[8].list.push({
    order: '4',
    methodId: '5b54f17a6044a4717f6b6baf6a70c448',
    desc: 'update',
});
api[8].list.push({
    order: '5',
    methodId: '4dbf75c24677a1f0f4a721e90cf91f83',
    desc: 'delete',
});
api.push({
    alias: 'SysLoginController',
    order: '10',
    desc: '登录验证',
    link: '登录验证',
    list: []
})
api[9].list.push({
    order: '1',
    methodId: 'f7d2d3f0f999f302c714e23e6f47f641',
    desc: '登录方法',
});
api[9].list.push({
    order: '2',
    methodId: '4c8abb3cbf0b13517c0f51c01514c8ba',
    desc: '获取用户信息',
});
api[9].list.push({
    order: '3',
    methodId: '97a629bc5df54f73d8b2b4c20179aead',
    desc: '获取路由信息',
});
api.push({
    alias: 'SysJobLogController',
    order: '11',
    desc: '调度日志操作处理',
    link: '调度日志操作处理',
    list: []
})
api[10].list.push({
    order: '1',
    methodId: '33360116e403e24821b5e0d854888e71',
    desc: '查询定时任务调度日志列表',
});
api[10].list.push({
    order: '2',
    methodId: '880453f97971ed9ff2930d8ee4f8d4c6',
    desc: '导出定时任务调度日志列表',
});
api[10].list.push({
    order: '3',
    methodId: '4e1090c701872fd9dca69071bdf01112',
    desc: '根据调度编号获取详细信息',
});
api[10].list.push({
    order: '4',
    methodId: '08042f536f63293af80e2576933af493',
    desc: '删除定时任务调度日志',
});
api[10].list.push({
    order: '5',
    methodId: '7b2aac08f5dcec4b3372dde2f7f0eda4',
    desc: '清空定时任务调度日志',
});
api.push({
    alias: 'RealTimeDataController',
    order: '12',
    desc: 'RealTimeDataController',
    link: 'realtimedatacontroller',
    list: []
})
api[11].list.push({
    order: '1',
    methodId: '80999c98853c604befbf692320b4e867',
    desc: '获取实时数据',
});
api.push({
    alias: 'CommonController',
    order: '13',
    desc: '通用请求处理',
    link: '通用请求处理',
    list: []
})
api[12].list.push({
    order: '1',
    methodId: '633fb59dc8c1420c133ff7446015466e',
    desc: '通用下载请求',
});
api[12].list.push({
    order: '2',
    methodId: '1ad603a633a552df9464d12975926102',
    desc: '通用上传请求（单个）',
});
api[12].list.push({
    order: '3',
    methodId: '281031e61e3f94e688c0718b245f46cd',
    desc: '通用上传请求（多个）',
});
api[12].list.push({
    order: '4',
    methodId: 'c7752c244d67c014216e6b0eda54248e',
    desc: '本地资源通用下载',
});
api.push({
    alias: 'SysUserController',
    order: '14',
    desc: '用户信息',
    link: '用户信息',
    list: []
})
api[13].list.push({
    order: '1',
    methodId: '000fc52805987a1a6937e13ece39224b',
    desc: '获取用户列表',
});
api[13].list.push({
    order: '2',
    methodId: '6309a619fcf99b181dbab9a9edc12f86',
    desc: 'export',
});
api[13].list.push({
    order: '3',
    methodId: '614dfcf272a728d70a3b131af8ed2ffa',
    desc: 'importData',
});
api[13].list.push({
    order: '4',
    methodId: 'a544f6b73359317b7865c4dc2b6a9ef9',
    desc: 'importTemplate',
});
api[13].list.push({
    order: '5',
    methodId: '92beb60e9010e6f81c359f245403d7ac',
    desc: '根据用户编号获取详细信息',
});
api[13].list.push({
    order: '6',
    methodId: 'e11cb972781f44a70b5d4f6a4b9cddd9',
    desc: '新增用户',
});
api[13].list.push({
    order: '7',
    methodId: '16fc3fc8cc2487a93acc4e5adb22431d',
    desc: '修改用户',
});
api[13].list.push({
    order: '8',
    methodId: '1a8085e389fe2c5b9e85492f8c424e57',
    desc: '删除用户',
});
api[13].list.push({
    order: '9',
    methodId: 'f8d5d652cebd5b50083a7aee6822f065',
    desc: '重置密码',
});
api[13].list.push({
    order: '10',
    methodId: 'f9e9a216e4fb4a697cb434522d73c155',
    desc: '状态修改',
});
api[13].list.push({
    order: '11',
    methodId: '449109132a74a531b19e7329014b1965',
    desc: '根据用户编号获取授权角色',
});
api[13].list.push({
    order: '12',
    methodId: '6caad3f33a80a2c3dda6ba0e721937cc',
    desc: '用户授权角色',
});
api[13].list.push({
    order: '13',
    methodId: '4fb87f4e7e37457062e6a081c08d397d',
    desc: '获取部门树列表',
});
api.push({
    alias: 'SysNoticeController',
    order: '15',
    desc: '公告 信息操作处理',
    link: '公告_信息操作处理',
    list: []
})
api[14].list.push({
    order: '1',
    methodId: 'e7cc902775bd4d3844163247be7fcd13',
    desc: '获取通知公告列表',
});
api[14].list.push({
    order: '2',
    methodId: '83abb576536cb3c694d39ec882f041bf',
    desc: '根据通知公告编号获取详细信息',
});
api[14].list.push({
    order: '3',
    methodId: '3de0da0372d9fa68e03de822c6422d05',
    desc: '新增通知公告',
});
api[14].list.push({
    order: '4',
    methodId: '08d1ab3782b8b8bfdd834643ab276a5c',
    desc: '修改通知公告',
});
api[14].list.push({
    order: '5',
    methodId: 'bcfbfb32d08f5b03aee00acb34034f1b',
    desc: '删除通知公告',
});
api.push({
    alias: 'SysPostController',
    order: '16',
    desc: '岗位信息操作处理',
    link: '岗位信息操作处理',
    list: []
})
api[15].list.push({
    order: '1',
    methodId: '931818cd3ff4823934aad65ba3b4d4fd',
    desc: '获取岗位列表',
});
api[15].list.push({
    order: '2',
    methodId: '752d300714321a4636c8f9bfa2b185d8',
    desc: 'export',
});
api[15].list.push({
    order: '3',
    methodId: 'ba88b8900effbf10f4a83bfbc1420824',
    desc: '根据岗位编号获取详细信息',
});
api[15].list.push({
    order: '4',
    methodId: '253364d4dbc8625e9fe4cfd8a4926a14',
    desc: '新增岗位',
});
api[15].list.push({
    order: '5',
    methodId: '041ed5057fdab38a9d2b55c5fabc633d',
    desc: '修改岗位',
});
api[15].list.push({
    order: '6',
    methodId: '784c36ce0c527cb2662b05d2dc9ee319',
    desc: '删除岗位',
});
api[15].list.push({
    order: '7',
    methodId: '6d69d69d2db82256ac64059fa009a24c',
    desc: '获取岗位选择框列表',
});
api.push({
    alias: 'SysLogininforController',
    order: '17',
    desc: '系统访问记录',
    link: '系统访问记录',
    list: []
})
api[16].list.push({
    order: '1',
    methodId: '3607f1d9902755f9d9f008394090cdb5',
    desc: 'list',
});
api[16].list.push({
    order: '2',
    methodId: 'e0fa6ce382347a29667a3097058b01dc',
    desc: 'export',
});
api[16].list.push({
    order: '3',
    methodId: '383b5f7b774623d1d78e08e6d0a4c67c',
    desc: 'remove',
});
api[16].list.push({
    order: '4',
    methodId: 'c9660fe31feb40cbd6a3d13676e6313e',
    desc: 'clean',
});
api[16].list.push({
    order: '5',
    methodId: '82119cd626ca7f816310f71682ec8fab',
    desc: 'unlock',
});
api.push({
    alias: 'ServerController',
    order: '18',
    desc: '服务器监控',
    link: '服务器监控',
    list: []
})
api[17].list.push({
    order: '1',
    methodId: '714741238241d0c41195b6ff18b08507',
    desc: 'getInfo',
});
api.push({
    alias: 'GenController',
    order: '19',
    desc: '代码生成 操作处理',
    link: '代码生成_操作处理',
    list: []
})
api[18].list.push({
    order: '1',
    methodId: '8ba2b21d7bb6d9394d8e4e7b47fdc6a4',
    desc: '查询代码生成列表',
});
api[18].list.push({
    order: '2',
    methodId: '88f5c5974539594822e835da9bd33bab',
    desc: '获取代码生成信息',
});
api[18].list.push({
    order: '3',
    methodId: '0ae56728c2c6f7926e9a491e12e5baf5',
    desc: '查询数据库列表',
});
api[18].list.push({
    order: '4',
    methodId: 'bb0f06457ac13c4ae03493bfd18918a0',
    desc: '查询数据表字段列表',
});
api[18].list.push({
    order: '5',
    methodId: '58f81da086e69f6d0861283557900c23',
    desc: '导入表结构（保存）',
});
api[18].list.push({
    order: '6',
    methodId: '9da0b86425c24d774fe359a81a13fe87',
    desc: '创建表结构（保存）',
});
api[18].list.push({
    order: '7',
    methodId: '5a4fadc0d4f4189c1adf78f09db10ce1',
    desc: '修改保存代码生成业务',
});
api[18].list.push({
    order: '8',
    methodId: 'b7d0a663f982d5cc0dba6c7be5025cb4',
    desc: '删除代码生成',
});
api[18].list.push({
    order: '9',
    methodId: 'b103db675f03a6531afabaea56a2f088',
    desc: '预览代码',
});
api[18].list.push({
    order: '10',
    methodId: 'f0b98ae3bddb47e0f1513cadb5e6fef7',
    desc: '生成代码（下载方式）',
});
api[18].list.push({
    order: '11',
    methodId: '70e3479f44af6ea9beeefe2896b5862e',
    desc: '生成代码（自定义路径）',
});
api[18].list.push({
    order: '12',
    methodId: '801fa9e37685991d22eca798d6c51e6d',
    desc: '同步数据库',
});
api[18].list.push({
    order: '13',
    methodId: '3d35a4fbba2ce4f1408784cc60f6ac0e',
    desc: '批量生成代码',
});
api.push({
    alias: 'SysDeptController',
    order: '20',
    desc: '部门信息',
    link: '部门信息',
    list: []
})
api[19].list.push({
    order: '1',
    methodId: '5dd4b35b68283db3a34d27e97cc2a658',
    desc: '获取部门列表',
});
api[19].list.push({
    order: '2',
    methodId: 'c1dee0e0a5a2c78ea94f02a812572de0',
    desc: '查询部门列表（排除节点）',
});
api[19].list.push({
    order: '3',
    methodId: '636638edfd4cccb96a723aa22c118454',
    desc: '根据部门编号获取详细信息',
});
api[19].list.push({
    order: '4',
    methodId: '080728d28286773d6f92ed7c884a5196',
    desc: '新增部门',
});
api[19].list.push({
    order: '5',
    methodId: '76eb807c42da25c29b10d7af8f7ae64d',
    desc: '修改部门',
});
api[19].list.push({
    order: '6',
    methodId: '347e88ce06fe447aee33f3e1f6363e0e',
    desc: '删除部门',
});
api.push({
    alias: 'SysConfigController',
    order: '21',
    desc: '参数配置 信息操作处理',
    link: '参数配置_信息操作处理',
    list: []
})
api[20].list.push({
    order: '1',
    methodId: 'd84fe9541bcf3086b323f169f5966f22',
    desc: '获取参数配置列表',
});
api[20].list.push({
    order: '2',
    methodId: 'd797d5ba9025aa3eb0149578c5791857',
    desc: 'export',
});
api[20].list.push({
    order: '3',
    methodId: '5225c9e7abc0d6ca4a1e51c7c4417fc6',
    desc: '根据参数编号获取详细信息',
});
api[20].list.push({
    order: '4',
    methodId: '2b8f759f811b0637e00ada6eaf2717b7',
    desc: '根据参数键名查询参数值',
});
api[20].list.push({
    order: '5',
    methodId: '03c0b4529e3c73a7b2d9e627ddc0a7bf',
    desc: '新增参数配置',
});
api[20].list.push({
    order: '6',
    methodId: 'a2b7fb8a1f0a67b408c3114a27a4b7ac',
    desc: '修改参数配置',
});
api[20].list.push({
    order: '7',
    methodId: 'f121cd26e5a4ac98e4c52b756e406407',
    desc: '删除参数配置',
});
api[20].list.push({
    order: '8',
    methodId: '7c81de755da55063e17497b76816d738',
    desc: '刷新参数缓存',
});
api.push({
    alias: 'HistoryDataController',
    order: '22',
    desc: '历史数据控制器  处理Modbus历史数据的分页查询请求',
    link: '历史数据控制器  处理modbus历史数据的分页查询请求',
    list: []
})
api[21].list.push({
    order: '1',
    methodId: 'c48b611075c0822d70fe8b2b030b828d',
    desc: '获取历史数据列表',
});
api.push({
    alias: 'CaptchaController',
    order: '23',
    desc: '验证码操作处理',
    link: '验证码操作处理',
    list: []
})
api[22].list.push({
    order: '1',
    methodId: '42b55d27256da30ab2ac3ea2538c0d88',
    desc: '生成验证码',
});
api.push({
    alias: 'SysDictTypeController',
    order: '24',
    desc: '数据字典信息',
    link: '数据字典信息',
    list: []
})
api[23].list.push({
    order: '1',
    methodId: 'bd47a5567232bda345baf854524bddf2',
    desc: 'list',
});
api[23].list.push({
    order: '2',
    methodId: '66796b05965d61eec70500328ad9b1f9',
    desc: 'export',
});
api[23].list.push({
    order: '3',
    methodId: '6c29807f4b3c3b81451b1fa1865e89d6',
    desc: '查询字典类型详细',
});
api[23].list.push({
    order: '4',
    methodId: '323c69edd1ec4f1c9fb6f44daf04e807',
    desc: '新增字典类型',
});
api[23].list.push({
    order: '5',
    methodId: 'b638cc43e0ea28ddd38c5723a014a7ce',
    desc: '修改字典类型',
});
api[23].list.push({
    order: '6',
    methodId: 'b2ed450f678e7c355a0d9b3bb83a0635',
    desc: '删除字典类型',
});
api[23].list.push({
    order: '7',
    methodId: '35c3aaa6a1b03cf6a90bb63758e816ec',
    desc: '刷新字典缓存',
});
api[23].list.push({
    order: '8',
    methodId: 'ab668c4d92bd2f48c1a8783a5ba075ee',
    desc: '获取字典选择框列表',
});
api.push({
    alias: 'SysOperlogController',
    order: '25',
    desc: '操作日志记录',
    link: '操作日志记录',
    list: []
})
api[24].list.push({
    order: '1',
    methodId: '27678a2483f830a89906c3375c8a852f',
    desc: 'list',
});
api[24].list.push({
    order: '2',
    methodId: '3ee87484da86fb7333daaa38aabb25e5',
    desc: 'export',
});
api[24].list.push({
    order: '3',
    methodId: '027b1183dcdd38396bd191c394b090a2',
    desc: 'remove',
});
api[24].list.push({
    order: '4',
    methodId: '92be7afdc4f52f7d7ae9a496dca7eeea',
    desc: 'clean',
});
api.push({
    alias: 'SysDictDataController',
    order: '26',
    desc: '数据字典信息',
    link: '数据字典信息',
    list: []
})
api[25].list.push({
    order: '1',
    methodId: 'fc3970ffd7b146ba4c21ec58536362af',
    desc: 'list',
});
api[25].list.push({
    order: '2',
    methodId: 'f53f340e8a1c60d79f674ed86553a322',
    desc: 'export',
});
api[25].list.push({
    order: '3',
    methodId: '81ac4b09b7ed3b88bbcaabda25699a6d',
    desc: '查询字典数据详细',
});
api[25].list.push({
    order: '4',
    methodId: '2e4dd4dc2d4e93f90d46a85c2c3e2b5b',
    desc: '根据字典类型查询字典数据信息',
});
api[25].list.push({
    order: '5',
    methodId: 'd27e19a72fe5af75c7d2d1dc5801eea9',
    desc: '新增字典类型',
});
api[25].list.push({
    order: '6',
    methodId: 'b1bd26cf422a7c31a6c14f64f91be3c2',
    desc: '修改保存字典类型',
});
api[25].list.push({
    order: '7',
    methodId: '1707f23525570fc581fde0b093c35094',
    desc: '删除字典类型',
});
document.onkeydown = keyDownSearch;
function keyDownSearch(e) {
    const theEvent = e;
    const code = theEvent.keyCode || theEvent.which || theEvent.charCode;
    if (code === 13) {
        const search = document.getElementById('search');
        const searchValue = search.value;
        let searchArr = [];
        for (let i = 0; i < api.length; i++) {
            let apiData = api[i];
            const desc = apiData.desc;
            if (desc.toLocaleLowerCase().indexOf(searchValue) > -1) {
                searchArr.push({
                    order: apiData.order,
                    desc: apiData.desc,
                    link: apiData.link,
                    alias: apiData.alias,
                    list: apiData.list
                });
            } else {
                let methodList = apiData.list || [];
                let methodListTemp = [];
                for (let j = 0; j < methodList.length; j++) {
                    const methodData = methodList[j];
                    const methodDesc = methodData.desc;
                    if (methodDesc.toLocaleLowerCase().indexOf(searchValue) > -1) {
                        methodListTemp.push(methodData);
                        break;
                    }
                }
                if (methodListTemp.length > 0) {
                    const data = {
                        order: apiData.order,
                        desc: apiData.desc,
                        alias: apiData.alias,
                        link: apiData.link,
                        list: methodListTemp
                    };
                    searchArr.push(data);
                }
            }
        }
        let html;
        if (searchValue === '') {
            const liClass = "";
            const display = "display: none";
            html = buildAccordion(api,liClass,display);
            document.getElementById('accordion').innerHTML = html;
        } else {
            const liClass = "open";
            const display = "display: block";
            html = buildAccordion(searchArr,liClass,display);
            document.getElementById('accordion').innerHTML = html;
        }
        const Accordion = function (el, multiple) {
            this.el = el || {};
            this.multiple = multiple || false;
            const links = this.el.find('.dd');
            links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown);
        };
        Accordion.prototype.dropdown = function (e) {
            const $el = e.data.el;
            let $this = $(this), $next = $this.next();
            $next.slideToggle();
            $this.parent().toggleClass('open');
            if (!e.data.multiple) {
                $el.find('.submenu').not($next).slideUp("20").parent().removeClass('open');
            }
        };
        new Accordion($('#accordion'), false);
    }
}

function buildAccordion(apiData, liClass, display) {
    let html = "";
    if (apiData.length > 0) {
         for (let j = 0; j < apiData.length; j++) {
            html += '<li class="'+liClass+'">';
            html += '<a class="dd" href="#' + apiData[j].alias + '">' + apiData[j].order + '.&nbsp;' + apiData[j].desc + '</a>';
            html += '<ul class="sectlevel2" style="'+display+'">';
            let doc = apiData[j].list;
            for (let m = 0; m < doc.length; m++) {
                html += '<li><a href="#' + doc[m].methodId + '">' + apiData[j].order + '.' + doc[m].order + '.&nbsp;' + doc[m].desc + '</a> </li>';
            }
            html += '</ul>';
            html += '</li>';
        }
    }
    return html;
}