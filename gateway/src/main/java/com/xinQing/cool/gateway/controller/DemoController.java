package com.xinQing.cool.gateway.controller;

import com.xinQing.cool.common.conf.Controller;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.stereotype.Component;

/**
 * demo
 *
 * Created by null on 2017/9/2.
 */
@Component
public class DemoController implements Controller {

    @Override
    public void register(Router router) {
        router.get("/demo").handler(this::show);
    }

    private void show(RoutingContext rc) {
        rc.response().end("Hello Gateway!");
    }
}