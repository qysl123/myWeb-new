package com.zk.elasticSearch;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * ElasticSearch 测试类
 * Created by Ken on 2017/2/21.
 */
public class ElasticSearchTest {
    public static void main(String[] args) {
        try {

            //设置集群名称
            Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
            //创建client
            TransportClient client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
            //搜索数据
//            GetResponse response = client.prepareGet("test", "tt", "1").execute().actionGet();
            Map<String, String> map = new HashMap<>();
            map.put("user", "123");
            map.put("message", "haha");
            IndexResponse response1 = client.prepareIndex("test", "tt").setSource(JSONObject.toJSONString(map)).get();
            QueryBuilder qb2= QueryBuilders.multiMatchQuery("lanqiu", "about");
            SearchResponse response = client.prepareSearch("haoche").setTypes("user").setQuery(qb2).execute()
                    .actionGet();
            SearchHits hits = response.getHits();
            //输出结果
            System.out.println(JSONObject.toJSONString(hits));
            //关闭client
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
