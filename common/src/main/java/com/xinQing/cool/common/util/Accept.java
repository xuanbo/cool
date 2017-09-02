package com.xinQing.cool.common.util;

import io.vertx.ext.web.Route;

/**
 * 设置接受的content-type
 *
 * Created by null on 2017/9/2.
 */
public class Accept {

    public static Route json(Route route) {
        return route.consumes(ContentType.APPLICATION_JSON.value()).produces(ContentType.APPLICATION_JSON.value());
    }

}