package com.hiekn.crawler.uyint.http;

import org.apache.log4j.Logger;

/**
 * Created by govert on 2016/1/25.
 */
public interface HttpReader {

    Logger log = Logger.getLogger(HttpReader.class);

    String readSource(String url);

    String readSource(String url, String charset);

    String readSource(String url, String charset, String cookies);

    void close();

}
