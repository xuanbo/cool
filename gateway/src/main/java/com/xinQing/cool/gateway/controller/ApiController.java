package com.xinQing.cool.gateway.controller;

import com.xinQing.cool.common.conf.Controller;
import com.xinQing.cool.common.discovery.Discovery;
import com.xinQing.cool.common.util.Accept;
import com.xinQing.cool.common.util.Response;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * api
 *
 * Created by null on 2017/9/2.
 */
@Component
public class ApiController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private Discovery discovery;

    @Override
    public void register(final Router router) {
        final String matchUrl = "/api/:discoveryName/*";
        Accept.json(router.get(matchUrl)).handler(this::doGet);
        Accept.json(router.post(matchUrl)).handler(this::doPost);
        Accept.json(router.put(matchUrl)).handler(this::doPut);
        Accept.json(router.delete(matchUrl)).handler(this::doDelete);
    }

    private void doGet(RoutingContext rc) {
        HttpServerRequest request = rc.request();
        // 服务名
        String name = request.getParam("discoveryName");
        // 服务uri
        String uri = request.uri().replaceFirst("/api/" + name, "");
        log.info("name[{}],uri[{}]", name, uri);
        discovery.doGet(name, uri, ar -> Response.sendJson(rc, ar.result()));
    }

    private void doPost(RoutingContext rc) {
        HttpServerRequest request = rc.request();
        // 服务名
        String name = request.getParam("discoveryName");
        // 服务uri
        String uri = request.uri().replaceFirst("/api/" + name, "");
        Buffer data = rc.getBody();
        log.info("name[{}],uri[{}],data[{}]", name, uri, data);
        discovery.doPost(name, uri, data, ar -> Response.sendJson(rc, ar.result()));
    }

    private void doPut(RoutingContext rc) {
        HttpServerRequest request = rc.request();
        // 服务名
        String name = request.getParam("discoveryName");
        // 服务uri
        String uri = request.uri().replaceFirst("/api/" + name, "");
        Buffer data = rc.getBody();
        log.info("name[{}],uri[{}],data[{}]", name, uri, data);
        discovery.doPut(name, uri, data, ar -> Response.sendJson(rc, ar.result()));
    }

    private void doDelete(RoutingContext rc) {
        HttpServerRequest request = rc.request();
        // 服务名
        String name = request.getParam("discoveryName");
        // 服务uri
        String uri = request.uri().replaceFirst("/api/" + name, "");
        Buffer data = rc.getBody();
        log.info("name[{}],uri[{}],data[{}]", name, uri, data);
        discovery.doDelete(name, uri, data, ar -> Response.sendJson(rc, ar.result()));
    }

}