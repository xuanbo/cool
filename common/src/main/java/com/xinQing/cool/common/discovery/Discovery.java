package com.xinQing.cool.common.discovery;

import com.xinQing.cool.common.util.ContentType;
import com.xinQing.cool.common.util.Result;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 服务发现
 *
 * Created by null on 2017/9/2.
 */
@Component
public class Discovery {

    private static final Logger log = LoggerFactory.getLogger(Discovery.class);

    private ServiceDiscovery serviceDiscovery;

    @Autowired
    private RedisServiceDiscoveryOptions redisServiceDiscoveryOptions;

    // the service name
    @Value("${discovery.name}")
    private String name;

    // the host, must be public
    @Value("${server.host:127.0.0.1}")
    private String host;

    // the port
    @Value("${server.port:8080}")
    private int port;

    // the root, if not set "/" is used
    @Value("${discovery.root:/}")
    private String root;

    public Discovery create(Vertx vertx) {
        serviceDiscovery = ServiceDiscovery.create(vertx, redisServiceDiscoveryOptions.create());
        return this;
    }

    public ServiceDiscovery getServiceDiscovery() {
        return serviceDiscovery;
    }

    public void publish() {
        Record record = HttpEndpoint.createRecord(name, host, port, root);
        serviceDiscovery.publish(record, ar -> {
            if (ar.succeeded()) {
                Record result = ar.result();
                log.info("服务发现发布成功 => {}", result.toJson());
            } else {
                log.warn("服务发现发布失败 => {}", ar.cause());
            }
        });
    }

    /**
     * 服务调用
     *
     * @param name 服务名称
     * @param handler Handler<AsyncResult<JsonObject>>
     */
    public void doGet(final String name, final String uri, final Handler<AsyncResult<String>> handler) {
        HttpEndpoint.getWebClient(serviceDiscovery, new JsonObject().put("name", name), ar -> {
            if (ar.succeeded()) {
                WebClient client = ar.result();
                client.get(uri).timeout(5000L).putHeader(ContentType.NAME, ContentType.APPLICATION_JSON.value())
                        .send(asyncResult -> {
                            if (asyncResult.succeeded()) {
                                ok(asyncResult.result().bodyAsString(), handler);
                            } else {
                                log.warn("服务发现异常 => {}", asyncResult.cause());
                                fail(ar.cause(), handler);
                            }
                });
                ServiceDiscovery.releaseServiceObject(serviceDiscovery, client);
            } else {
                log.warn("服务发现异常 => {}", ar.cause());
                fail(ar.cause(), handler);
            }
        });
    }

    /**
     * 服务调用
     *
     * @param name 服务名称
     * @param handler Handler<AsyncResult<JsonObject>>
     */
    public void doPost(final String name, final String uri, final Buffer data, final Handler<AsyncResult<String>> handler) {
        HttpEndpoint.getWebClient(serviceDiscovery, new JsonObject().put("name", name), ar -> {
            if (ar.succeeded()) {
                WebClient client = ar.result();
                doHttpBody(client.post(uri), data, handler);
                ServiceDiscovery.releaseServiceObject(serviceDiscovery, client);
            } else {
                log.warn("服务发现异常 => {}", ar.cause());
                fail(ar.cause(), handler);
            }
        });
    }

    /**
     * 服务调用
     *
     * @param name 服务名称
     * @param handler Handler<AsyncResult<JsonObject>>
     */
    public void doPut(final String name, final String uri, final Buffer data, final Handler<AsyncResult<String>> handler) {
        HttpEndpoint.getWebClient(serviceDiscovery, new JsonObject().put("name", name), ar -> {
            if (ar.succeeded()) {
                WebClient client = ar.result();
                doHttpBody(client.put(uri), data, handler);
                ServiceDiscovery.releaseServiceObject(serviceDiscovery, client);
            } else {
                log.warn("服务发现异常 => {}", ar.cause());
                fail(ar.cause(), handler);
            }
        });
    }

    /**
     * 服务调用
     *
     * @param name 服务名称
     * @param handler Handler<AsyncResult<JsonObject>>
     */
    public void doDelete(final String name, final String uri, final Buffer data, final Handler<AsyncResult<String>> handler) {
        HttpEndpoint.getWebClient(serviceDiscovery, new JsonObject().put("name", name), ar -> {
            if (ar.succeeded()) {
                WebClient client = ar.result();
                doHttpBody(client.delete(uri), data, handler);
                ServiceDiscovery.releaseServiceObject(serviceDiscovery, client);
            } else {
                log.warn("服务发现异常 => {}", ar.cause());
                fail(ar.cause(), handler);
            }
        });
    }

    private void doHttpBody(final HttpRequest<Buffer> request, final Buffer data, final Handler<AsyncResult<String>> handler) {
        request.timeout(5000L).sendBuffer(data, ar -> {
            if (ar.succeeded()) {
                ok(ar.result().bodyAsString(), handler);
            } else {
                log.warn("服务发现异常 => {}", ar.cause());
                fail(ar.cause(), handler);
            }
        });
    }

    private void ok(String data, final Handler<AsyncResult<String>> handler) {
        Future.succeededFuture(data).setHandler(handler);
    }

    private void fail(Throwable t, final Handler<AsyncResult<String>> handler) {
        Future.succeededFuture(Result.fail(t.getMessage()).toJson()).setHandler(handler);
    }

    /**
     * 关闭服务发现
     */
    public void close() {
        serviceDiscovery.close();
    }

    /**
     * 所有服务记录
     */
    public void records() {
        serviceDiscovery.getRecords(new JsonObject().put("name", "*"), ar -> {
            if (ar.succeeded()) {
               ar.result().forEach(record -> log.info("{}", record.toJson()));
            }
        });
    }

}