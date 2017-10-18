package com.iflytek.jrshi.ViewRedis;


public class ExceptionUtil {

    public static String getAllTraceInfo(Exception e) {
        if (e == null) return null;
        StackTraceElement[] traces = e.getStackTrace();
        if (traces == null || traces.length == 0) return null;

        StringBuilder sb = ThreadLocalVariables.stringBuilder1024.get();
        sb.setLength(0);
        sb.append("[\"").append(traces[0].toString()).append("\"");
        for (int i = 1; i < traces.length; i++) {
            sb.append(" ,\"").append(traces[i].toString()).append("\"");
        }
        sb.append("]");

        return sb.toString();
    }

    public static String getTopTraceInfo(Exception e) {
        if (e == null) return null;
        StackTraceElement[] traces = e.getStackTrace();
        if (traces == null || traces.length == 0) return null;
        return traces[0].toString();
    }

}
