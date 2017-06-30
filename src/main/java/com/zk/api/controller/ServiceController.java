package com.zk.api.controller;


import com.zk.api.helper.ApiDefinition;
import com.zk.api.helper.RequestWarper;
import com.zk.api.helper.ServiceConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by werewolf on 2017/2/17.
 */
@Controller
@RequestMapping("/api")
public class ServiceController extends BaseController {


    private static int i = 0;

    @ResponseBody
    @RequestMapping(value = "/{service}")
    public Object service(@PathVariable String service,
                          @RequestParam(value = "version") String version,
                          @RequestParam(value = "platform", required = false) String platform,
                          @RequestParam(value = "data", required = false) String data,
                          @RequestParam(value = "timestamp") Long timestamp,
                          @RequestParam(value = "salt") String salt,
                          @RequestParam(value = "sign") String sign,
                          HttpServletRequest request, HttpServletResponse response,@RequestParam(required = false,value = "filedata") MultipartFile file) {


        i = i + 1;
        System.out.println("------------------" + i);
        System.out.println("version:" + version);
        System.out.println("platform:" + platform);
        System.out.println("data:" + data);
        System.out.println("timestamp:" + timestamp);
        System.out.println("salt:" + salt);
        System.out.println("sign:" + sign);

        return handler(new RequestWarper()
                .setService(service)
                .setVersion(version)
                .setPlatform("1")
                .setData(data)
                .setTimestamp(timestamp)
                .setSalt(salt)
                .setSign(sign)
                .setRequest(request)
                .setResponse(response)
                .setCheckSign(true)
                .setEncodeResponse(ServiceConfig.find(service).getRequest())
                .setDecodeRequest(ServiceConfig.find(service).getResponse())
                .setDataKey(ApiDefinition.Key.APP_KEY_DATA)
                .setSignKey(ApiDefinition.Key.APP_KEY_SIGN)
                .setFile(file)
        );
    }


    @ResponseBody
    @RequestMapping(value = "/test/{service}")
    public Object test(@PathVariable String service,
                       @RequestParam(value = "version") String version,
                       @RequestParam(value = "platform", required = false) String platform,
                       @RequestParam(value = "data", required = false) String data,
                       @RequestParam(value = "timestamp", required = false) Long timestamp,
                       @RequestParam(value = "salt", required = false) String salt,
                       @RequestParam(value = "sign", required = false) String sign,
                       HttpServletRequest request, HttpServletResponse response,@RequestParam(required = false,value = "filedata") MultipartFile file) {


        return handler(new RequestWarper()
                .setService(service)
                .setVersion(version)
                .setPlatform("1")
                .setData(data)
                .setTimestamp(timestamp)
                .setSalt(salt)
                .setSign(sign)
                .setRequest(request)
                .setResponse(response)
                .setCheckSign(false)
                .setEncodeResponse(false)
                .setDecodeRequest(false)
                .setFile(file)
        );
    }

}
