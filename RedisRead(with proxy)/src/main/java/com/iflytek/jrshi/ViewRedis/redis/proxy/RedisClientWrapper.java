package com.iflytek.jrshi.ViewRedis.redis.proxy;


import com.iflytek.jrshi.ViewRedis.redis.RedisClient;

public class RedisClientWrapper extends RedisClient {

    public RedisClientWrapper(String redisClientName) {
        super(redisClientName);
    }

    public RedisClientWrapper() {
    }

    private long totalRequestLastSample; // 上一次采样记录的总请求数
    private long totalExceptionLastSample; // 上一次采样记录的总异常数
    private long timestampLastSample; // 上一次采样的时间点

    private boolean online = true;
    private int weight = 1;

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public long getTotalRequestLastSample() {
        return totalRequestLastSample;
    }

    public void setTotalRequestLastSample(long totalRequestLastSample) {
        this.totalRequestLastSample = totalRequestLastSample;
    }

    public long getTotalExceptionLastSample() {
        return totalExceptionLastSample;
    }

    public void setTotalExceptionLastSample(long totalExceptionLastSample) {
        this.totalExceptionLastSample = totalExceptionLastSample;
    }

    public long getTimestampLastSample() {
        return timestampLastSample;
    }

    public void setTimestampLastSample(long timestampLastSample) {
        this.timestampLastSample = timestampLastSample;
    }

}
