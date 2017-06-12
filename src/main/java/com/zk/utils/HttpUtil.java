package com.zk.utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * bird on 2016/12/14.
 */
public class HttpUtil {

    public static String getUrlBuilder(String url, Map param){
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        Set<String> keys = param.keySet();
        for (String name : keys) {
            Object value = param.get(name);
            BasicNameValuePair nameValuePair = new BasicNameValuePair(name, value.toString());
            params.add(nameValuePair);
        }
        String queryString = URLEncodedUtils.format(params, "UTF-8");
        return url + "?" + queryString;
    }

    public static String httpGet(String url){
        HttpGet get = new HttpGet(url);
        HttpClient client = HttpClients.createDefault();
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String httpGet(String url,Map map){
        HttpGet get = new HttpGet(getUrlBuilder(url,map));
        HttpClient client = HttpClients.createDefault();
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(" http://123.56.137.232:8081/api/userBankFirst?data={%22userId%22:1}&service=userBankFirst&version=1.0&platform=FENSEBOOK".substring(50));
    }
}
