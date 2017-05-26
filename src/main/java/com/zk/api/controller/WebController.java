package com.zk.api.controller;


import com.zk.api.helper.RequestWarper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/api")
public class WebController extends BaseController {

    static Logger logger = LoggerFactory.getLogger(WebController.class.getName());

    @ResponseBody
    @RequestMapping("/{service}")
    public Object index(HttpServletRequest request,
                        @PathVariable(value = "service") String service,
                        @RequestParam(value = "version") String version,
                        @RequestParam(value = "platform") String platform,
                        @RequestParam(value = "data", required = false) String json) {
        return handler(new RequestWarper()
                .setService(service)
                .setVersion(version)
                .setPlatform(platform)
                .setData(json).setRequest(request)
        );
    }
}
