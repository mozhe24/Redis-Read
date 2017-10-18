package com.iflytek.jrshi.ViewRedis;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * 定义一些线程相关的常用的对象，减少对象的分配 </br>
 * 注意：这些对象不要嵌套使用，嵌套会破坏对象数据
 * <p>
 * 优化建议：</br>
 * 对于使用频次高、作用域小、不在线程间传递的对象（比如工具类），可以在此创建，减少对象频繁分配带来的创建和垃圾回收的消耗
 *
 * @author xdlv
 */
public class ThreadLocalVariables {

    /* 使用前先调用setLength(0) */
    public static ThreadLocal<StringBuilder> stringBuilder1024 = new ThreadLocal<StringBuilder>() {
        protected StringBuilder initialValue() {
            return new StringBuilder(1024);
        }
    };

    /* 使用前先调用setLength(0) */
    public static ThreadLocal<StringBuilder> stringBuilder256 = new ThreadLocal<StringBuilder>() {
        protected StringBuilder initialValue() {
            return new StringBuilder(256);
        }
    };

    /* 使用前先调用setLength(0) */
    public static ThreadLocal<StringBuilder> stringBuilder64 = new ThreadLocal<StringBuilder>() {
        protected StringBuilder initialValue() {
            return new StringBuilder(64);
        }
    };

    /* 使用前先调用setLength(0) */
    public static ThreadLocal<StringBuilder> stringBuilder16 = new ThreadLocal<StringBuilder>() {
        protected StringBuilder initialValue() {
            return new StringBuilder(16);
        }
    };

    /* 用于格式化毫秒时间戳，获取年、月、日、时、分、秒等信息，使用前调用setTime(long) */
    public static ThreadLocal<Date> date = new ThreadLocal<Date>() {
        protected Date initialValue() {
            return new Date();
        }
    };

    public static ThreadLocal<ByteArrayOutputStream> byteArrayOutputStream1024 = new ThreadLocal<ByteArrayOutputStream>() {
        protected ByteArrayOutputStream initialValue() {
            return new ByteArrayOutputStream(1024);
        }
    };

    public static ThreadLocal<Random> random = new ThreadLocal<Random>() {
        protected Random initialValue() {
            return new Random();
        }
    };

    public static ThreadLocal<ArrayList<String>> arrayListString = new ThreadLocal<ArrayList<String>>() {
        protected ArrayList<String> initialValue() {
            return new ArrayList<String>();
        }
    };

}
