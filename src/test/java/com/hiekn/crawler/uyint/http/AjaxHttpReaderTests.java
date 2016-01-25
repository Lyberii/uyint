package com.hiekn.crawler.uyint.http;

import com.hiekn.crawler.uyint.http.impl.AjaxHttpReader;
import com.hiekn.crawler.uyint.http.impl.StaticHttpReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;

/**
 * Created by govert on 2016/1/25.
 */
public class AjaxHttpReaderTests {

    private HttpReader httpReader;

    @Before
    public void init() throws MalformedURLException {
        System.out.println("init httpReader...");
        httpReader = new AjaxHttpReader();
    }


    @Test
    public void testReadSource() {
        String source = httpReader.readSource("http://toutiao.com");
        System.out.println(source);
    }

    @After
    public void close() {
        System.out.println("close httpReader...");
        httpReader.close();
    }
}
