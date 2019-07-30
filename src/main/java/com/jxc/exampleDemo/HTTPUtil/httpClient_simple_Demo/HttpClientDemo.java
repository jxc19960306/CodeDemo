package com.jxc.exampleDemo.HTTPUtil.httpClient_simple_Demo;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: export_parent
 * Role：
 *
 * @author Jxc
 * @version 1.0
 * @date 2019/7/17
 */
@SuppressWarnings("all")
public class HttpClientDemo {
    /**
     * 发送get 请求
     */
    @Test
    public void getRequest() throws IOException {
        // url
        String hostName = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //指定url 和 端口
        HttpGet httpPost = new HttpGet(hostName);
        //发送请求，获得响应结果
        CloseableHttpResponse execute = httpClient.execute(httpPost);

        //获得响应状态码
        StatusLine statusLine = execute.getStatusLine();
        System.out.println(statusLine.getStatusCode());
        //获得页面内容
        HttpEntity entity = execute.getEntity();
        String htmlContent = EntityUtils.toString(entity);
        System.out.println(htmlContent);

    }

    /**
     * 发送post 请求
     * ww9nVl1tvqfhjswscfoVq04M7aItPY;
     */
    //ww9nVlltvqfhjSWscfoVq04M7aItPY
    @Test
    public void postRequest() throws IOException {
        String hostName = "";
        List<NameValuePair> pairList = new ArrayList<>();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //指定url 和 端口
        HttpPost httpPost = new HttpPost(hostName);
        if (pairList != null && pairList.size() > 0) {
            //添加 要传的参数
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairList);
            httpPost.setEntity(formEntity);
        }
        //发送请求，获得响应结果
        CloseableHttpResponse execute = httpClient.execute(httpPost);

        //获得响应状态码
        StatusLine statusLine = execute.getStatusLine();
        System.out.println(statusLine.getStatusCode());
        //获得页面内容
        HttpEntity entity = execute.getEntity();
        String htmlContent = EntityUtils.toString(entity);
        System.out.println(htmlContent);
    }
}
