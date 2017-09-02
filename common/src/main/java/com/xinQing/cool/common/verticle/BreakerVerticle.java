package com.xinQing.cool.common.verticle;

import com.xinQing.cool.common.discovery.Breaker;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 提供断路器
 *
 * Created by null on 2017/9/2.
 */
@Component
public class BreakerVerticle extends StartVerticle {

    @Autowired
    private Breaker breaker;

    @Override
    protected void init(Vertx vertx) {
        breaker.create(vertx);
    }
}