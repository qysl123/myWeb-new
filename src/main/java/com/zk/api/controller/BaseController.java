package com.zk.api.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zk.api.helper.*;
import com.zk.api.spring.SpringApplicationContext;
import com.zk.api.validator.Validator;
import com.zk.api.validator.ValidatorException;
import com.zk.utils.DateUtils;
import com.zk.utils.Des3Utils;
import com.zk.utils.DigestUtils;
import com.zk.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;

public class BaseController {

    private static final String SYSTEM_TIME_SERVICE = "systemTime";

    static Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected Object handler(RequestWarper requestWarper) {
        try {

            if (SYSTEM_TIME_SERVICE.equals(requestWarper.getService())) {
                return doSystemTime();
            }


            IApiService apiService = SpringApplicationContext.getBean(requestWarper.getService() + requestWarper.getVersion());
            if (apiService == null) {
                return ApiResponse.fail(HttpStatus.NOT_SERVICE.getMessage(), HttpStatus.NOT_SERVICE.getCode());
            }

            //验证签名
            if (requestWarper.getCheckSign()) {
                String _sign = DigestUtils.MD5(requestWarper.getService() + requestWarper.getTimestamp() + requestWarper.getData() + requestWarper.getVersion() + requestWarper.getSalt() + requestWarper.getSignKey());
//                                 requestWarper.getService() + requestWarper.getTimestamp() + requestWarper.getData() + requestWarper.getVersion() + requestWarper.getSalt() + requestWarper.getSignKey();
                System.out.println(requestWarper.getService() + requestWarper.getTimestamp() + requestWarper.getData() + requestWarper.getVersion() + requestWarper.getSalt() + requestWarper.getSignKey());
                System.out.println(requestWarper.getSign());
                System.out.println(_sign);
                if (!_sign.equals(requestWarper.getSign())) {
                    //验证签名失败
                    return ApiResponse.fail(HttpStatus.SERVICE_FORBIDDEN.getMessage(), HttpStatus.SERVICE_FORBIDDEN.getCode());
                }

                //验证时间戳 15秒有效
                if (DateUtils.currentTimeSecs() - (requestWarper.getTimestamp() / 1000) > 60) {
                    return ApiResponse.fail(HttpStatus.TIME_OUT.getMessage(), HttpStatus.TIME_OUT.getCode());
                }
            }

            if (requestWarper.getDecodeRequest()) {
                //解密 data
                if (requestWarper.getData() != null) {
                    requestWarper.setData(new String(Des3Utils.decode(DigestUtils.MD5(requestWarper.getSalt() + requestWarper.getDataKey()),
                            DigestUtils.decodeBase64(requestWarper.getData().getBytes("utf-8"))), "utf-8"));
                }

            }

            //接口请求参数验证
            final Map requestParams = StringUtils.isEmpty(requestWarper.getData()) ? new HashMap() : JSON.parseObject(requestWarper.getData(), HashMap.class);
            requestParams.put("platform", requestWarper.getPlatform());
//            requestParams.put("sessionId", requestWarper.getRequest().getSession().getId());
            requestParams.put("ip", WebToolsUtils.ip(requestWarper.getRequest()));
            requestParams.put("files", requestWarper.getFile());
            Validator.validate(apiService.getClass(), requestParams);

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            Map result = apiService.service(requestParams);
            stopWatch.stop();

            logger.info(requestWarper.getService() + requestWarper.getVersion() + requestWarper.getPlatform() + "--execute time millis---" + stopWatch.getTotalTimeMillis());
            if (requestWarper.getEncodeResponse() && result.get("body") != null) {
                final String key = UUIDUtils.sn();
                result.put("key", key);
                result.put("body",
                        DigestUtils.encodeBase64(
                                Des3Utils.encode(
                                        DigestUtils.MD5(key + requestWarper.getDataKey()), JSON.toJSONString(result.get("body"), ApiResponse.filter, SerializerFeature.WriteMapNullValue)
                                )
                        )
                );
            }

            return result;
        } catch (ValidatorException e) {
            return ApiResponse.fail(e.getMessage(), HttpStatus.BAD_REQUEST.getCode());
        } catch (ApiException e) {
            logger.error("--apiException---" + e.getMessage());
            return ApiResponse.fail(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("--RuntimeException---" + e.getMessage());
            return ApiResponse.fail("系统错误");
        } catch (Exception e) {
            return ApiResponse.fail("系统错误");
        }
    }


    private Object doSystemTime() {
        return ApiResponse.success(new HashMap() {{
            put("timestamp", System.currentTimeMillis());
        }});
    }
}
