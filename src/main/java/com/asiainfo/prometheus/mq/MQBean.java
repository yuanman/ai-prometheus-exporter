package com.asiainfo.prometheus.mq;

public class MQBean {
    String topic ;
    int totalDiff;
    
    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }
    public int getTotalDiff() {
        return totalDiff;
    }
    public void setTotalDiff(int totalDiff) {
        this.totalDiff = totalDiff;
    }
}
