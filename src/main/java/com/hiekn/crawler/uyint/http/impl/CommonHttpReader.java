package com.hiekn.crawler.uyint.http.impl;

import com.hiekn.crawler.uyint.http.HttpReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by govert on 2016/1/25.
 */
public abstract class CommonHttpReader implements HttpReader {

    public String readSource(String url) {
        return readSource(url, null, null);
    }

    public String readSource(String url, String charset) {
        return readSource(url, charset, null);
    }

    public String parseCharset(String pageSource) {

        Matcher charsetMatcher = null;

        //<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        charsetMatcher = Pattern.compile("(?i)<meta.*?content=('|\").*?charset=(\\S+?)\\1.*?/?\\s?>").matcher(pageSource);
        if (charsetMatcher.find()) {
            return charsetMatcher.group(2).toLowerCase();
        }

        //<meta charset="gb2312" />
        charsetMatcher = Pattern.compile("(?i)<meta.*?charset=('|\")(\\S+?)\\1.*?/?\\s?>").matcher(pageSource);
        if (charsetMatcher.find()) {
            return charsetMatcher.group(2).toLowerCase();
        }

        // <meta http-equiv='Content-Language' content='utf-8'/>
        charsetMatcher = Pattern.compile("(?i)<meta.*?content=('|\")(\\S+?)\\1.*?/?\\s?>").matcher(pageSource);
        if (charsetMatcher.find()) {
            return charsetMatcher.group(2).toLowerCase();
        }

        return "";


    }
}
