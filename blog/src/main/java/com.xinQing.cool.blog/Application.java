package com.xinQing.cool.blog;

import com.xinQing.cool.common.verticle.StartVerticle;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

/**
 * 入口
 *
 * Created by null on 2017/9/2.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.xinQing.cool.blog", "com.xinQing.cool.common"})
public class Application {

    @Autowired
    private StartVerticle startVerticle;

    @PostConstruct
    public void start() {
        Vertx.vertx().deployVerticle(startVerticle);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}