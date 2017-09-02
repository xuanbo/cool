package com.xinQing.cool.common.discovery;

import com.xinQing.cool.common.util.ContentType;
import com.xinQing.cool.common.util.Result;
import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 熔断器
 *
 * Created by null on 2017/9/2.
 */
@Component
public class Breaker {

    private static final Logger log = LoggerFactory.getLogger(Breaker.class);

    private CircuitBreaker circuitBreaker;

    @Autowired
    private Discovery discovery;

    @Value("${breaker.name:circuit-breaker}")
    private String name;

    @Value("${breaker.maxFailures:5}")
    private int maxFailures;

    @Value("${breaker.timeout:5000}")
    private long timeout;

    @Value("${breaker.fallbackOnFailure:true}")
    private boolean fallbackOnFailure;

    @Value("${breaker.resetTimeout:10000}")
    private long resetTimeout;

    public CircuitBreaker create(Vertx vertx) {
        circuitBreaker = CircuitBreaker.create(name, vertx,
                new CircuitBreakerOptions()
                        // 最大故障次数
                        .setMaxFailures(maxFailures)
                        // 超时时间
                        .setTimeout(timeout)
                        // 设置是否失败回调
                        .setFallbackOnFailure(fallbackOnFailure)
                        // 重置状态超时
                        .setResetTimeout(resetTimeout)
        )
                // 开启断路器
                .openHandler(v -> log.info("Circuit opened"))
                // 关闭断路器
                .closeHandler(v -> log.warn("Circuit closed"));
        return circuitBreaker;
    }

    public void doGet(final String name, final String uri, final Handler<AsyncResult<String>> handler) {
        final ServiceDiscovery serviceDiscovery = discovery.getServiceDiscovery();
        circuitBreaker.executeWithFallback(future ->
                        HttpEndpoint.getWebClient(serviceDiscovery, new JsonObject().put("name", name), ar -> {
                            if (ar.succeeded()) {
                                WebClient client = ar.result();
                                client.get(uri).putHeader(ContentType.NAME, ContentType.APPLICATION_JSON.value())
                                        .send(asyncResult -> {
                                            if (asyncResult.succeeded()) {
                                                future.complete(asyncResult.result().bodyAsString());
                                            } else {
                                                log.warn("服务发现异常 => {}", asyncResult.cause());
                                                future.fail(asyncResult.cause());
                                            }
                                        });
                                ServiceDiscovery.releaseServiceObject(serviceDiscovery, client);
                            } else {
                                log.warn("服务发现异常 => {}", ar.cause());
                                future.fail(ar.cause());
                            }
                        })
                , v -> {
                    // 当熔断器熔断时将调用此处代码
                    return Result.fail(v.getMessage()).toJson();
                }).setHandler(ar -> Future.succeededFuture(ar.result()).setHandler(handler)); // 处理结果
    }

    public void doPost(final String name, final String uri, final Buffer data, final Handler<AsyncResult<String>> handler) {
        final ServiceDiscovery serviceDiscovery = discovery.getServiceDiscovery();
        circuitBreaker.executeWithFallback(future ->
                        HttpEndpoint.getWebClient(serviceDiscovery, new JsonObject().put("name", name), ar -> {
                            if (ar.succeeded()) {
                                WebClient client = ar.result();
                                doHttpBody(client.post(uri), data, future);
                                ServiceDiscovery.releaseServiceObject(serviceDiscovery, client);
                            } else {
                                log.warn("服务发现异常 => {}", ar.cause());
                                future.fail(ar.cause());
                            }
                        })
                , v -> {
                    // 当熔断器熔断时将调用此处代码
                    return Result.fail(v.getMessage()).toJson();
                }).setHandler(ar -> Future.succeededFuture(ar.result()).setHandler(handler)); // 处理结果
    }

    /**
     * 服务调用
     *
     * @param name    服务名称
     * @param handler Handler<AsyncResult<JsonObject>>
     */
    public void doPut(final String name, final String uri, final Buffer data, final Handler<AsyncResult<String>> handler) {
        final ServiceDiscovery serviceDiscovery = discovery.getServiceDiscovery();
        circuitBreaker.executeWithFallback(future ->
                        HttpEndpoint.getWebClient(serviceDiscovery, new JsonObject().put("name", name), ar -> {
                            if (ar.succeeded()) {
                                WebClient client = ar.result();
                                doHttpBody(client.put(uri), data, future);
                                ServiceDiscovery.releaseServiceObject(serviceDiscovery, client);
                            } else {
                                log.warn("服务发现异常 => {}", ar.cause());
                                future.fail(ar.cause());
                            }
                        })
                , v -> {
                    // 当熔断器熔断时将调用此处代码
                    return Result.fail(v.getMessage()).toJson();
                }).setHandler(ar -> Future.succeededFuture(ar.result()).setHandler(handler)); // 处理结果

    }

    /**
     * 服务调用
     *
     * @param name    服务名称
     * @param handler Handler<AsyncResult<JsonObject>>
     */
    public void doDelete(final String name, final String uri, final Buffer data, final Handler<AsyncResult<String>> handler) {
        final ServiceDiscovery serviceDiscovery = discovery.getServiceDiscovery();
        circuitBreaker.executeWithFallback(future ->
                        HttpEndpoint.getWebClient(serviceDiscovery, new JsonObject().put("name", name), ar -> {
                            if (ar.succeeded()) {
                                WebClient client = ar.result();
                                doHttpBody(client.delete(uri), data, future);
                                ServiceDiscovery.releaseServiceObject(serviceDiscovery, client);
                            } else {
                                log.warn("服务发现异常 => {}", ar.cause());
                                future.fail(ar.cause());
                            }
                        })
                , v -> {
                    // 当熔断器熔断时将调用此处代码
                    return Result.fail(v.getMessage()).toJson();
                }).setHandler(ar -> Future.succeededFuture(ar.result()).setHandler(handler)); // 处理结果

    }

    private void doHttpBody(final HttpRequest<Buffer> request, final Buffer data, final Future<String> future) {
        request.sendBuffer(data, ar -> {
            if (ar.succeeded()) {
                future.complete(ar.result().bodyAsString());
            } else {
                log.warn("服务发现异常 => {}", ar.cause());
                future.fail(ar.cause());
            }
        });
    }

}