package com.hiekn.crawler.uyint.util;

import com.hiekn.crawler.uyint.util.ConstResource;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by govert on 2016/1/25.
 */
public class ConstResourceTests extends TestCase {

    @Test
    public void testGet() {

        String url = ConstResource.get("remote.webdriver.url");
        assertEquals("http://192.168.2.120:27301", url);
    }

}
