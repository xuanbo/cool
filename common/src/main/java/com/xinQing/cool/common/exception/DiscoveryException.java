package com.xinQing.cool.common.exception;

/**
 * 服务发现异常
 *
 * Created by null on 2017/9/3.
 */
public class DiscoveryException extends Exception {

    public DiscoveryException() {
        this("服务发现异常");
    }

    public DiscoveryException(String message) {
        super(message);
    }
}