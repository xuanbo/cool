# 说明

## 技术要点

采用vert.x开发微服务，服务发现。

## 模块

* common: 公共模块
* gateway: 网关
* blog: blog服务

## 快速开始（本地运行）

默认情况下，gateway在本地8080端口，blog在本地9090端口。

对网关而言：会对请求`/api/服务注册名/xxx`拦截，分发到具体的服务，其中xxx为具体的服务地址。

* 启动redis，作为服务发现桥
* 启动gateway、blog（运行`Application.main`方法或者gateway、blog目录下在执行`gradle bootRun`）
* 访问gateway：`localhost:8080/api/blog/demo/1`，gateway会将请求分发到blog服务，与直接访问blog服务相同：`localhost:9090/demo/1`