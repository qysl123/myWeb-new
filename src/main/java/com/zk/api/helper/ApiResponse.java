package com.zk.api.helper;

import com.alibaba.fastjson.serializer.ValueFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by werewolf on 2017/2/19.
 */
public class ApiResponse {

    public static ValueFilter filter = (obj, s, v) -> {
        if (v == null || "null".equals(v)) {
            return "";
        }
        return v;
    };

    public static Map fail() {
        return response(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), HttpStatus.INTERNAL_SERVER_ERROR.getMessage(), null);
    }

    public static Map fail(final String message) {
        return response(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), message, null);
    }

    public static Map fail(final String message, final Integer code) {
        return response(code, message, null);
    }


    public static Map success() {
        return response(HttpStatus.SUCCESS.getCode(), HttpStatus.SUCCESS.getMessage(), null);
    }

    public static Map success(final String message) {
        return response(HttpStatus.SUCCESS.getCode(), message, null);
    }

    public static Map success(final Object obj) {
        if (obj == null) {
            return fail("not result", 400);
        }

        return response(HttpStatus.SUCCESS.getCode(), HttpStatus.SUCCESS.getMessage(), obj);
    }


    private static Map response(final Integer code, final String message, final Object body) {
        return new HashMap() {{
            put("status", code);
            put("message", message);
            if (body != null) {
                put("body", body);
            }
        }};
    }
}
