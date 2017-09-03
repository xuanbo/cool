package com.xinQing.cool.common.verticle;

import com.xinQing.cool.common.conf.Register;
import com.xinQing.cool.common.discovery.Discovery;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 启动服务
 *
 * Created by null on 2017/9/2.
 */
@Component
public class StartVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(StartVerticle.class);

    @Value("${server.port:8080}")
    private int port;

    @Autowired
    private Register register;

    @Autowired
    private Discovery discovery;

    /***
     * 子类做一些初始化操作
     *
     * @param vertx Vertx
     */
    protected void init(Vertx vertx) {
    }

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        Router router = register.router(vertx);
        // 服务发现，redis
        discovery.create(vertx)
                .publish();
        // 初始化
        init(vertx);
        server.requestHandler(router::accept).listen(port);
        log.info("start server on port:{}", port);
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        discovery.close(ar -> {
            if (ar.succeeded()) {
                stopFuture.complete();
            } else {
                stopFuture.fail(ar.cause());
            }
        });
    }
}