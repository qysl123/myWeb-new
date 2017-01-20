package com.zk.mina.modle;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Ken on 2017/1/20.
 */
public class SentBody implements Serializable {
    private String key;

    private HashMap<String, String> data;

    private long timestamp;

    public SentBody() {

        data = new HashMap<String, String>();
        timestamp = System.currentTimeMillis();
    }

    public String getKey() {
        return key;
    }

    public String get(String k) {
        return data.get(k);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void put(String k, String v) {
        data.put(k, v);
    }

    public void remove(String k) {
        data.remove(k);
    }

    public HashMap<String, String> getData() {
        return data;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        buffer.append("<sent>");
        buffer.append("<key>").append(key).append("</key>");
        buffer.append("<timestamp>").append(timestamp).append("</timestamp>");
        buffer.append("<data>");
        for (String key : data.keySet()) {
            buffer.append("<" + key + ">").append(data.get(key)).append(
                    "</" + key + ">");
        }
        buffer.append("</data>");
        buffer.append("</sent>");
        return buffer.toString();
    }

    public String toXmlString() {
        return toString();
    }
}
