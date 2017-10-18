package com.iflytek.jrshi.ViewRedis.redis.proxy;

public class ResourceNotFoundException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -5478253504070437267L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

    public ResourceNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

}
