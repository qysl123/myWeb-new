package com.zk.api.service;


import com.zk.api.helper.IApiService;
import org.eclipse.core.internal.resources.OS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * Created by werewolf on 2017/2/19.
 */
public abstract class AbstractApiService implements IApiService {


    protected static Logger log = LoggerFactory.getLogger(AbstractApiService.class);


    public int page(Map request) {
        return Convert.Int(Convert.Int(request.get("page"), 1));
    }


    public int limit(Map request) {
        return Convert.Int(Convert.Int(request.get("limit"), 10));
    }

    public Long userId(Map request) {
        return Convert.formatLong(request.get("userId"));
    }


    public Long bookId(Map request) {
        if (null == request.get("bookId")) {
            return null;
        }
        return Convert.formatLong(request.get("bookId"));
    }

    public Long chapterId(Map request) {
        if (null == request.get("chapterId")) {
            return null;
        }
        return Convert.formatLong(request.get("chapterId"));
    }


    public long id(Map request) {
        return Convert.formatLong(request.get("id"));
    }


    public static class Convert {

        public static String String(Object obj) {
            if (obj == null) {
                return null;
            } else {
                return String.valueOf(obj);
            }
        }

        public static String String(Object obj, String defaultValue) {
            if (obj == null) {
                return defaultValue;
            } else {
                return String.valueOf(obj);
            }
        }

        public static Long formatLong(Object obj) {
            if (obj == null) {
                return null;
            } else {
                return Long.valueOf(String.valueOf(obj));
            }
        }

        public static Long formatLong(Object obj, long value) {
            if (obj == null) {
                return value;
            } else {
                return Long.valueOf(String.valueOf(obj));
            }
        }


        public static int Int(Object obj) {
            if (obj == null) {
                return 0;
            } else {
                return Integer.valueOf(String.valueOf(obj));
            }
        }

        public static int Int(Object obj, int value) {
            if (obj == null) {
                return value;
            } else {
                return Integer.valueOf(String.valueOf(obj));
            }
        }

        public static Integer Integer(Object obj) {
            if (obj == null) {
                return null;
            } else {
                return Integer.valueOf(String.valueOf(obj));
            }
        }
    }


}