package com.xinQing.cool.common.exception;

/**
 * 找不到服务发现记录异常
 *
 * Created by null on 2017/9/3.
 */
public class NoServiceDiscoveryException extends DiscoveryException {

    public NoServiceDiscoveryException() {
        this("找不到服务发现记录");
    }

    public NoServiceDiscoveryException(String message) {
        super(message);
    }
}