package com.asiainfo.prometheus.redis;

public class RedisBean {
    public String instanceId;
    public String instanceName;
    public String usedMemCache;
    public String capacity; // 单位：Mbyte。

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getUsedMemCache() {
        return usedMemCache;
    }

    public void setUsedMemCache(String usedMemCache) {
        this.usedMemCache = usedMemCache;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

}
