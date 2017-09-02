package com.xinQing.cool.common.util;

/**
 * ContentType
 *
 * Created by null on 2017/9/2.
 */
public enum ContentType {

    APPLICATION_JSON("application/json");

    private String value;

    public static String NAME = "content-type";

    ContentType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}