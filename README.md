# easy.server

#### 项目简介

easy.server 是一个屏蔽网络消息交互协议的公用业务逻辑的项目框，基于其构建的项目，其底层交互协议可以是TCP ，HTTP
等不同来源，而自身业务层可以做到完全公用或者少量的修改。

[技术详细](doc)

## 特性

- 抽离业务实现，屏蔽交互协议影响
- 基于 ASM 等字节码技术， 实现高性能消息映射到实体类和函数反射。
- swagger 模块，方便开发时等业务调试与技术对接
- 底层基于Spring，易于技术拓展

## 项目结构

- [easy.server.mapper](easy.server.mapper) 基于ASM ，Javassist，用于消息映射与函数反射模块
- [easy.server.utils](easy.server.utils) 工具包模块，提供 json库和 log 日志操作
- [easy.server.core](easy.server.core) 核心实现，屏蔽消息交互格式提供业务的抽象隔离
- [easy.server.cache](easy.server.cache) 提供业务缓存使用功能
- [easy.server.swagger](easy.server.swagger) 开发调试和前后端对接
- [plugins](plugins) maven 插件 用于 easy.server.mapper 代码的自动生成，提高初始加载时的性能
- [easy.server.demo](easy.server.demo)easy.server.mapper 项目示例 提供了基于 spring+tomcat ，springboot web，netty tcp 的示例

