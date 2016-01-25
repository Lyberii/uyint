package com.hiekn.crawler.uyint.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by govert on 2016/1/25.
 */
public class ConstResource {

    static Properties props = new Properties();

    static {
        InputStream in = null;
        try {

            in = ConstResource.class.getClassLoader().getResourceAsStream("uyint.properties");
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static final String REMOTE_WEBDRIVER_URL = get("remote.webdriver.url");


    public static String get(String key) {
        return props.getProperty(key);
    }

}
