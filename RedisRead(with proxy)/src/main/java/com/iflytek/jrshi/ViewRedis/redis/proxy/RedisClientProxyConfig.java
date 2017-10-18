package com.iflytek.jrshi.ViewRedis.redis.proxy;

import java.util.Set;

public class RedisClientProxyConfig {

    private String proxyName;
    private Set<String> nodes; // ip:port:weight
    private String password;
    private int maxActive = 10;
    private int maxIdle = 10;
    private long maxWaitMillis = 1000;
    private int connectionTimeout = 1000;
    private int socketTimeout = 1000;
    private long timeBetweenEviction = 0;
    private boolean testOnBorrow = false;
    private boolean testWhileIdle = false;

    public Set<String> getNodes() {
        return nodes;
    }

    public void setNodes(Set<String> nodes) {
        this.nodes = nodes;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public long getTimeBetweenEviction() {
        return timeBetweenEviction;
    }

    public void setTimeBetweenEviction(long timeBetweenEviction) {
        this.timeBetweenEviction = timeBetweenEviction;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public String getProxyName() {
        return proxyName;
    }

    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

}
