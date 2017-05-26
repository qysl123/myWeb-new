package com.zk.api.validator;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhongkun on 2017/5/26.
 */
public class Validator {
    private static final Map<Class, AnnotationValidator> map = new HashMap() {
        {
            this.put(NotNull.class, new NotNullValidator());
            this.put(NotNulls.class, new NotNullsValidator());
        }
    };

    public Validator() {
    }

    public static Boolean validate(Class service, Map request) {
        Annotation[] annotations = service.getAnnotations();
        Annotation[] var3 = annotations;
        int var4 = annotations.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Annotation annotation = var3[var5];
            if(map.get(annotation.annotationType()) != null) {
                ((AnnotationValidator)map.get(annotation.annotationType())).handler(annotation, request);
            }
        }

        return Boolean.valueOf(true);
    }
}
