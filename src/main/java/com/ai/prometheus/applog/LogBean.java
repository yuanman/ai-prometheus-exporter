package com.ai.prometheus.applog;

public class LogBean {
    private String index;
    private String ip;
    private int exceptionAmout;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getExceptionAmout() {
        return exceptionAmout;
    }

    public void setExceptionAmout(int exceptionAmout) {
        this.exceptionAmout = exceptionAmout;
    }
}
