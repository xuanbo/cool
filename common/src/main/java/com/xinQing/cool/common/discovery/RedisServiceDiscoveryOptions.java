package com.xinQing.cool.common.discovery;

import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 基于redis的服务发现
 *
 * Created by null on 2017/9/2.
 */
@Component
public class RedisServiceDiscoveryOptions {

    @Value("${discovery.redis.host:127.0.0.1}")
    private String host;

    @Value("${discovery.redis.key:records}")
    private String key;

    public ServiceDiscoveryOptions create() {
        return new ServiceDiscoveryOptions()
                .setBackendConfiguration(
                        new JsonObject()
                                .put("host", host)
                                .put("key", key)
                );
    }

}
