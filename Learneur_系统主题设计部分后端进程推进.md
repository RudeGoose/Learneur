2022-12-01 推进进程:
- 建立数据库: 协商关于知识点和学习笔记两个数据表之间的关联(check)
- JWT操作以及验证登录操作的实现(check)
- 部分工具类的实现(check)
- Java Mail邮件发送的实现(主要包括验证码邮件的实现以及忘记密码验证码邮件的实现)(check)

2022-12-02 推进进程:
- 完善用户登录和注册操作(check)
- 对于用户信息(checked), 学习笔记相关信息的CRUD操作实现

2022-12-03 推进流程:
- 完善笔记相关信息的CRUD操作实现(checked)和具体资源的CRUD操作实现(checked)
- 实现具体资源和笔记相关的API接口实现(checked)

2022-12-04 推进流程:
- 完善抽象资源的CRUD及相关API操作(checked)
- 完善知识点CRUD及相关API操作
- 通过AOP实现部分辅助功能(checked - 资源部分日志记录以及全局异常处理)

2022-12-05 推进流程:
- 知识点相关查询(重点, 不清楚的地方需在注释标明)(checked)

文件结构部分修改操作:
- 2022-12-01 用户数据库部分增添其所属角色id字段
- 2022-12-02 学习笔记数据库部分增添笔记内容字段
- 2022-12-03 笔记部分考虑到浏览和点赞等操作多用户同时发生, 添加乐观锁字段
- 2022-12-03 添加点赞数据库, 存放点赞关系
- 2022-12-04 添加知识点下的资源字段, 不存放数据库