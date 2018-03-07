package com.asiainfo.prometheus.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "srv")
@Component
public class ServiceConfig {
    private String accesskey;
    private String securityKey;
    
    //for mq
    private String mqPlatform;
    private String mqUrl;
    private String mqRegionId;
    private String mqConsumerId;
    private String mqTopicDetail;

    //for rds
    private String rdsFormat;
    private String rdsVersion;
    private String rdsSignatureMethod;
    private String rdsSignatureVersion;
    private String rdsUrl;
    private String rdsAction;
    private String rdsDBInstanceId;
    
    public String getAccesskey() {
        return accesskey;
    }
    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }
    public String getSecurityKey() {
        return securityKey;
    }
    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }
    public String getMqPlatform() {
        return mqPlatform;
    }
    public void setMqPlatform(String mqPlatform) {
        this.mqPlatform = mqPlatform;
    }
    public String getMqUrl() {
        return mqUrl;
    }
    public void setMqUrl(String mqUrl) {
        this.mqUrl = mqUrl;
    }
    public String getMqRegionId() {
        return mqRegionId;
    }
    public void setMqRegionId(String mqRegionId) {
        this.mqRegionId = mqRegionId;
    }
    public String getMqConsumerId() {
        return mqConsumerId;
    }
    public void setMqConsumerId(String mqConsumerId) {
        this.mqConsumerId = mqConsumerId;
    }
    public String getMqTopicDetail() {
        return mqTopicDetail;
    }
    public void setMqTopicDetail(String mqTopicDetail) {
        this.mqTopicDetail = mqTopicDetail;
    }
    public String getRdsFormat() {
        return rdsFormat;
    }
    public void setRdsFormat(String rdsFormat) {
        this.rdsFormat = rdsFormat;
    }
    public String getRdsVersion() {
        return rdsVersion;
    }
    public void setRdsVersion(String rdsVersion) {
        this.rdsVersion = rdsVersion;
    }
    public String getRdsSignatureMethod() {
        return rdsSignatureMethod;
    }
    public void setRdsSignatureMethod(String rdsSignatureMethod) {
        this.rdsSignatureMethod = rdsSignatureMethod;
    }
    public String getRdsSignatureVersion() {
        return rdsSignatureVersion;
    }
    public void setRdsSignatureVersion(String rdsSignatureVersion) {
        this.rdsSignatureVersion = rdsSignatureVersion;
    }
    public String getRdsUrl() {
        return rdsUrl;
    }
    public void setRdsUrl(String rdsUrl) {
        this.rdsUrl = rdsUrl;
    }
    public String getRdsAction() {
        return rdsAction;
    }
    public void setRdsAction(String rdsAction) {
        this.rdsAction = rdsAction;
    }
    public String getRdsDBInstanceId() {
        return rdsDBInstanceId;
    }
    public void setRdsDBInstanceId(String rdsDBInstanceId) {
        this.rdsDBInstanceId = rdsDBInstanceId;
    }
        
}
