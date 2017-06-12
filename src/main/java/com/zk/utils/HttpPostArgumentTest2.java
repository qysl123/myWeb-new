package com.zk.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.HttpStatus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhongkun on 2017/6/12.
 */
public class HttpPostArgumentTest2 {
    //file1与file2在同一个文件夹下 filepath是该文件夹指定的路径
    public void SubmitPost() {

        Long timestamp = System.currentTimeMillis();
        Map<String, String> param = new HashMap<String, String>() {{
            put("version", "1.0");
            put("platform", "");
            put("timestamp", String.valueOf(timestamp));
            put("salt", "123456");
        }};
        String _sign = DigestUtils.MD5("upload" + String.valueOf(timestamp) + param.get("data") + param.get("version") + param.get("salt") + "8HkocpYLeG1LNi5mNN00");
        param.put("sign", _sign);
        String url = "http://localhost:8080/api/upload";

        String targetURL = null;// TODO 指定URL
        File targetFile = null;// TODO 指定上传文件
        targetFile = new File("d:\\1.jpg");
        targetURL = HttpUtil.getUrlBuilder(url, param);
        PostMethod filePost = new PostMethod(targetURL);
//          filePost.setRequestHeader("Content-type", "multipart/form-data");
        try {
            Part[] parts = {
                    new FilePart("filedata", targetFile)
//                    new StringPart("version", "1.0"),
//                    new StringPart("platform", ""),
//                    new StringPart("timestamp", String.valueOf(timestamp)),
//                    new StringPart("salt", "123456"),
//                    new StringPart("sign", _sign),
            };
            filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
            HttpClient client = new HttpClient();
            client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
            int status = client.executeMethod(filePost);
            if (status == HttpStatus.SC_OK) {
                System.out.println("上传成功");
                System.out.println(filePost.getResponseBodyAsString());
                // 上传成功
            } else {
                System.out.println("上传失败:"+status);
                System.out.println(filePost.getResponseBodyAsString());
                // 上传失败
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            filePost.releaseConnection();
        }

    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        HttpPostArgumentTest2 httpPostArgumentTest2 = new HttpPostArgumentTest2();
        httpPostArgumentTest2.SubmitPost();
    }
}
