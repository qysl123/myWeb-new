package com.zk.api.validator;

import java.lang.annotation.*;

/**
 * Created by zhongkun on 2017/5/26.
 */
@Repeatable(NotNulls.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {
    String[] name();

    String message() default "not null";

    String[] match() default {"", ""};
}
