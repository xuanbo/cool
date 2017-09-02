package com.xinQing.cool.common.verticle;

import com.xinQing.cool.common.conf.Register;
import com.xinQing.cool.common.discovery.Discovery;
import com.xinQing.cool.common.discovery.RedisServiceDiscoveryOptions;
import io.vertx.core.AbstractVerticle;
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

    @Autowired
    private RedisServiceDiscoveryOptions redisServiceDiscoveryOptions;

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        Router router = register.router(vertx);
        // 服务发现，redis
        discovery.create(vertx, redisServiceDiscoveryOptions.create())
                .publish();
        server.requestHandler(router::accept).listen(port);
        log.info("start server on port:{}", port);
    }

    @Override
    public void stop() throws Exception {
        discovery.close();
    }
}