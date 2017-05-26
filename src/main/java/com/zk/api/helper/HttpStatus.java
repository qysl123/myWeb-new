package com.zk.api.helper;

/**
 * Created by werewolf on 2017/2/19.
 */
public enum HttpStatus {

    SUCCESS(200, "success"),
    BAD_REQUEST(400, "bad request"),
    NOT_SERVICE(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVICE_FORBIDDEN(403, "service forbidden"),
    TIME_OUT(405, "timeout");


    private final int code;

    private final String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
