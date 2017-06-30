package com.zk.api.helper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by werewolf on 2017/2/20.
 */
public class ServiceConfig {

    private String service;
    private Boolean request;
    private Boolean response;


    public ServiceConfig() {

    }

    public ServiceConfig(String service, Boolean request, Boolean response) {
        this.service = service;
        this.request = request;
        this.response = response;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Boolean getRequest() {
        return request;
    }

    public void setRequest(Boolean request) {
        this.request = request;
    }

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    private static Map<String, ServiceConfig> table = new HashMap() {{
        put("login", new ServiceConfig("login", true, true));
        put("tLogin", new ServiceConfig("tLogin", false, false));
        put("bookCategoryRecommendList", new ServiceConfig("bookCategoryRecommendList", true, true));
        put("purchaseVerifyReceipt", new ServiceConfig("purchaseVerifyReceipt", true, true));
    }};

    public static List<ServiceConfig> all() {
        List<ServiceConfig> configs = new ArrayList<>();
        table.forEach((k, v) -> configs.add(v));
        return configs;
    }

    public static ServiceConfig find(String service) {
        return table.get(service) == null ? new ServiceConfig(service, false, false) : table.get(service);
    }

}
