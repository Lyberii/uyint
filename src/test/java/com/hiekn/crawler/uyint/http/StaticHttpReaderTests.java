package com.hiekn.crawler.uyint.http;

import com.hiekn.crawler.uyint.http.HttpReader;
import com.hiekn.crawler.uyint.http.impl.StaticHttpReader;
import org.junit.*;

/**
 * Created by govert on 2016/1/25.
 */
public class StaticHttpReaderTests {

    private HttpReader httpReader;

    @Before
    public void init() {
        System.out.println("init httpReader...");
        httpReader = new StaticHttpReader();
    }


    @Test
    public void testReadSource() {
        String source = httpReader.readSource("http://blog.csdn.net/jason0539/article/details/45110355", "utf-8");
        System.out.println(source);
    }

    @After
    public void close() {
        System.out.println("close httpReader...");
        httpReader.close();
    }
}
