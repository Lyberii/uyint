package com.hiekn.crawler.uyint.http.impl;

import com.hiekn.crawler.uyint.http.HttpReader;
import com.hiekn.crawler.uyint.util.ConstResource;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by govert on 2016/1/25.
 *
 * @author pzn
 * @since 1.7
 * @version 1.0
 */
public class AjaxHttpReader extends CommonHttpReader implements HttpReader {

    private WebDriver webDriver;
    private static String remoteWebdriverUrl;

    public AjaxHttpReader() throws MalformedURLException {
        this.remoteWebdriverUrl = ConstResource.REMOTE_WEBDRIVER_URL;
        this.webDriver = new RemoteWebDriver(new URL(remoteWebdriverUrl), DesiredCapabilities.phantomjs());
    }

    public String readSource(String url, String charset, String cookies) {

        log.info("request url start ... " + url);

        if (null != cookies && !"".equals(cookies)) {
            String[] cs = cookies.split(";");
            for (String c : cs) {
                String[] ckv = c.split("=");
                webDriver.manage().addCookie(new Cookie(ckv[0], ckv[1]));
            }

        }

        webDriver.get(url);

        webDriver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        webDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        String pageSource = webDriver.getPageSource();

        log.info("request result length ... " + pageSource.length());
        log.info("request url done ... " + url);

        return pageSource;
    }

    public void close() {
        if (null != webDriver) {
            webDriver.quit();
        }
    }
}
