package com.yahaha.ad.utils;

import com.yahaha.ad.exception.AdException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

/**
 * @Auther LeeMZ
 * @Date 2021/2/7
 **/
public class CommonUtils {

    //支持的时间转化的字符串类型
    private static String[] parsePatterns = {
        "yyyy-MM-dd","yyyy/MM/dd","yyyy.MM.dd"
    };

    //根据user生成md5字符串token
    public static String md5(String value){
        return DigestUtils.md5Hex(value).toUpperCase();
    }

    //将string类型的字符串转为Date类型
    public static Date parseStringDate(String dateString) throws AdException {
        try {
            return DateUtils.parseDate(dateString,parsePatterns);
        }catch (Exception e){
            throw new AdException(e.getMessage());
        }
    }
}
