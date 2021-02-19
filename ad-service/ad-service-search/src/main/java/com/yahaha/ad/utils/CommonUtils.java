package com.yahaha.ad.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @Auther LeeMZ
 * @Date 2021/2/11
 **/
@Slf4j
public class CommonUtils {

    public static <K,V> V getOrCreate(K key, Map<K,V> map, Supplier<V> factory){

        return map.computeIfAbsent(key,k->factory.get());
    }

    public static String stringContact(String... args){

        StringBuilder result = new StringBuilder();
        for (String arg : args) {
            result.append(arg);
            result.append("-");
        }
        result.deleteCharAt(result.length() - 1);

        return result.toString();
    }

    // Tue Jan 01 08:00:00 CST 2019
    public static Date parseStringDate(String dateString){

        try{
            DateFormat dateFormat = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss zzz yyy",
                    Locale.US
            );

            return DateUtils.addHours(dateFormat.parse(dateString),-8);

        }catch (ParseException ex){
            log.error("parseStringDate error: {}",dateString);
            return null;
        }

    }
}
