package com.zk.api.helper;


import java.util.Map;

/**
 * Created by werewolf on 2017/2/19.
 */

public interface IApiService<K extends Map> {
    K service(Map request);
}

