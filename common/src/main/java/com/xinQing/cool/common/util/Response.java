package com.xinQing.cool.common.util;

import io.vertx.ext.web.RoutingContext;

/**
 * 输出到httpResponse
 *
 * Created by null on 2017/9/2.
 */
public class Response {

    public static void sendJson(RoutingContext rc, String data) {
        rc.response().putHeader(ContentType.NAME, ContentType.APPLICATION_JSON.value()).end(data);
    }

    public static <T> void sendJson(RoutingContext rc, Result<T> result) {
        rc.response().putHeader(ContentType.NAME, ContentType.APPLICATION_JSON.value()).end(result.toJson());
    }

}