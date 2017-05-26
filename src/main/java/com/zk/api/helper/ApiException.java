package com.zk.api.helper;

/**
 * Created by werewolf on 2016/11/24.
 */
public class ApiException extends RuntimeException{

    //Internal Server Error
    private Integer code = HttpStatus.INTERNAL_SERVER_ERROR.getCode();

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ApiException() {
    }

    public ApiException(String message) {
        super(message);
    }


    public ApiException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public ApiException(String message, Integer code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ApiException(Throwable cause) {
        super(cause);
    }
}
