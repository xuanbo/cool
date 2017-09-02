package com.xinQing.cool.common.conf;

import io.vertx.ext.web.Router;

/**
 * 控制器实现此接口，注册路由
 *
 * Created by null on 2017/9/2.
 */
public interface Controller {

    void register(Router router);

}