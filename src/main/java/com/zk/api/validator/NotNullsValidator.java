package com.zk.api.validator;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Created by zhongkun on 2017/5/26.
 */
public class NotNullsValidator implements AnnotationValidator {
    public NotNullsValidator() {
    }

    public void handler(Annotation annotation, Map request) {
        NotNulls notNulls = (NotNulls)annotation;
        NotNull[] var4 = notNulls.value();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            NotNull notNull = var4[var6];
            (new NotNullValidator()).handler(notNull, request);
        }

    }
}
