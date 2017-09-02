package com.xinQing.cool.blog.controller;

import com.xinQing.cool.common.conf.Controller;
import com.xinQing.cool.common.util.Response;
import com.xinQing.cool.common.util.Result;
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
        router.get("/demo/:id").handler(this::show);
        router.post("/demo").handler(this::save);
    }

    private void show(RoutingContext rc) {
        Response.sendJson(rc, Result.ok("Hello Blog Demo!"));
    }

    private void save(RoutingContext rc) {
        Response.sendJson(rc, Result.ok(rc.getBodyAsJson()));
    }
}