package com.zk.api.validator;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Created by zhongkun on 2017/5/26.
 */
public interface AnnotationValidator {
    void handler(Annotation var1, Map var2);
}
