package com.hiekn.crawler.uyint.http.impl;

import com.hiekn.crawler.uyint.http.HttpReader;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Random;

/**
 * Created by govert on 2016/1/25.
 *
 * @author pzn
 * @since 1.7
 * @version 1.0
 */
public class StaticHttpReader extends CommonHttpReader implements HttpReader {

    CloseableHttpClient httpClient = null;
    RequestConfig defaultRequestConfig = null;
    int tries = 3;
    String defaultCharset = "gbk";

    String[] userAgents = {
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36",// chrome
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0",// firefox
            "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko"// ie
    };

    public StaticHttpReader() {
        httpClient = HttpClients.custom()//
                .setUserAgent(userAgents[new Random().nextInt(3)])//
                .build();

        defaultRequestConfig = RequestConfig.custom()//
                .setConnectTimeout(15000)//连接超时时间
                .setSocketTimeout(15000)//传输超时时间
                .setConnectionRequestTimeout(15000)//请求超时时间
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();
    }

    public StaticHttpReader(String hostname, int port) {
        httpClient = HttpClients.custom()//
                .setUserAgent(userAgents[new Random().nextInt(3)])//
                .build();

        defaultRequestConfig = RequestConfig.custom()//
                .setConnectTimeout(15000)//连接超时时间
                .setSocketTimeout(15000)//传输超时时间
                .setConnectionRequestTimeout(15000)//请求超时时间
                .setProxy(new HttpHost(hostname, port))// 设置代理
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();
    }


    public String readSource(String url, String charset, String cookies) {
        CloseableHttpResponse httpResponse = null;
        String pageSource = "";
        log.info("request url start ... " + url);
        for (int tryTime = 0; tryTime < tries; tryTime++) {
            try {
                HttpGet httpGet = new HttpGet(url);
                httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                httpGet.addHeader("Accept-Encoding", "gzip, deflate, sdch");
                httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.6");
                httpGet.addHeader("Connection", "keep-alive");
                if (null != cookies && !"".equals(cookies)) {
                    httpGet.addHeader("Cookie", cookies);
                }
                httpGet.setConfig(defaultRequestConfig);
                httpResponse = httpClient.execute(httpGet);

                int status = httpResponse.getStatusLine().getStatusCode();
                log.info("response code ... " + status);
                if (HttpStatus.SC_OK == status) {
                    HttpEntity entity = httpResponse.getEntity();
                    if (null != entity) {

                        if (charset != null && !"".equals(charset)) {
                            pageSource = EntityUtils.toString(entity, charset);
                        }
                        else {//解析编码
                            ContentType contentType = ContentType.get(entity);
                            String contentCharset = null;
                            if (contentType != null) {
                                if (contentType.getCharset() != null) {
                                    contentCharset = contentType.getCharset().displayName();
                                }
                            }
                            // 服务器没有返回url指定的编码,先使用ISO-8859-1,然后从源码找查找编码
                            if (contentCharset == null) {
                                contentCharset = Consts.ISO_8859_1.displayName();

                                //EntityUtils.toString先在entity中找编码，没找到，使用传入的默认编码。没设置默认编码，则使用ISO_8859_1
                                pageSource = EntityUtils.toString(entity, contentCharset);

                                log.info("解析  " + url + " 编码 ...start");
                                charset = parseCharset(pageSource).equals("") ? defaultCharset :  parseCharset(pageSource);//解析到的编码
                                log.info("解析 " + url + " 编码...done...charset=" + charset);

                                pageSource = new String(pageSource.getBytes(contentCharset), charset);
                            }
                            else {//如果服务器返回时有指定编码，则不会出现乱码
                                charset = contentCharset;

                                pageSource = EntityUtils.toString(entity, charset);
                            }
                        }
                        //entity 不为null，结束循环
                        break;
                    }
                }
            }  catch (Exception e) {
                log.error(url + " 源码读取失败 ..." + e.getMessage());
            } finally {
                if (httpResponse != null) {
                    try {
                        httpResponse.close();
                    } catch (IOException e) {
                        log.error("IOException " + e);
                    }
                }
            }
        }
        log.info("request result length ... " + pageSource.length());
        log.info("request url done ... " + url);
        return pageSource;
    }

    public void close() {
        if (null != httpClient) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();

                log.error("close ... " + e);
            }
            httpClient = null;
        }
    }
}
