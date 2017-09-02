package com.xinQing.cool.common.conf;

import com.xinQing.cool.common.util.ApplicationContextAdapter;
import com.xinQing.cool.common.util.Response;
import com.xinQing.cool.common.util.Result;
import io.vertx.core.Vertx;
import io.vertx.core.json.DecodeException;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 注册路由，构建Router对象
 *
 * Created by null on 2017/9/2.
 */
@Component
public class Register {

    private static final Logger log = LoggerFactory.getLogger(Register.class);

    @Autowired
    private ApplicationContextAdapter applicationContextAdapter;

    public Router router(Vertx vertx) {
        List<Controller> controllers = applicationContextAdapter.getBeans(Controller.class);
        return router(vertx, controllers);
    }

    private Router router(Vertx vertx, List<Controller> controllers) {
        final Router router = Router.router(vertx);

        // parse body
        router.route().handler(BodyHandler.create());

        // 注册路由
        controllers.forEach(controller -> {
            log.info("register router[{}]", controller.getClass());
            controller.register(router);
        });

        // not found
        router.route().handler(rc -> Response.sendJson(rc, Result.notFound()));

        // 全局异常处理
        router.route().failureHandler(rc -> {
            int code = rc.statusCode();
            if (rc.failure() == null) {
                Response.sendJson(rc, Result.fail(code));
            } else {
                Throwable t = rc.failure();
                log.warn("全局异常捕获 => {}", t);
                if (t instanceof DecodeException) {
                    Response.sendJson(rc, Result.fail(400, "错误请求"));
                    return;
                }
                String message = t.getMessage();
                Response.sendJson(rc, Result.fail(code, message));
            }
        });
        return router;
    }

}