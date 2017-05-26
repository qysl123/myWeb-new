package com.zk.api.validator;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Created by zhongkun on 2017/5/26.
 */
public class NotNullValidator implements AnnotationValidator {
    public NotNullValidator() {
    }

    public void handler(Annotation annotation, Map request) {
        NotNull notNull = (NotNull)annotation;
        String[] names = notNull.name();
        String[] var5 = names;
        int var6 = names.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            String n = var5[var7];
            if(notNull.match()[0].equals("")) {
                if(request.get(n) == null) {
                    throw new ValidatorException(notNull.message());
                }
            } else if(String.valueOf(request.get(notNull.match()[0])).equals(notNull.match()[1]) && request.get(n) == null) {
                throw new ValidatorException(notNull.message());
            }
        }

    }
}
