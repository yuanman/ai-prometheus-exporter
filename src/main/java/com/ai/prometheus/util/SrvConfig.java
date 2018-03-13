package com.ai.prometheus.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "srv")
@Component
public class SrvConfig {
    private String accesskey;
    private String securityKey;

    // for mq
    private String mqPlatform;
    private String mqUrl;
    private String mqRegionId;
    private String mqConsumerId;
    private String mqTopicDetail;

    // for rds
    private String rdsFormat;
    private String rdsVersion;
    private String rdsSignatureMethod;
    private String rdsSignatureVersion;
    private String rdsUrl;
    private String rdsAction;
    private String rdsDBInstanceId;
    private String rdsDBName;

    // for slb
    private String slbFormat;
    private String slbVersion;
    private String slbSignatureMethod;
    private String slbSignatureVersion;
    private String slbUrl;
    private String slbAction;
    private String slbLoadBalancerId;
    private String slbListenerPort;

    // for redis
    private String redisFormat;
    private String redisVersion;
    private String redisSignatureMethod;
    private String redisSignatureVersion;
    private String redisUrl;
    private String redisDescribeInstances;

    // for rdsList
    private List<Map<String, Object>> rdsList = new ArrayList<>();

    public List<Map<String, Object>> getRdsList() {
        return rdsList;
    }

    public void setRdsList(List<Map<String, Object>> rdsList) {
        this.rdsList = rdsList;
    }
    
    // for slbList
    private List<Map<String, Object>> slbList = new ArrayList<>();
    
    
    public List<Map<String, Object>> getSlbList() {
        return slbList;
    }

    public void setSlbList(List<Map<String, Object>> slbList) {
        this.slbList = slbList;
    }

    public String getRedisDescribeInstances() {
        return redisDescribeInstances;
    }

    public void setRedisDescribeInstances(String redisDescribeInstances) {
        this.redisDescribeInstances = redisDescribeInstances;
    }

    private String redisDescribeHistoryMonitorValues;
    private String redisInstanceId;
    private String redisIntervalForHistory;

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

    public String getRdsDBName() {
        return rdsDBName;
    }

    public void setRdsDBName(String rdsDBName) {
        this.rdsDBName = rdsDBName;
    }

    public String getSlbFormat() {
        return slbFormat;
    }

    public void setSlbFormat(String slbFormat) {
        this.slbFormat = slbFormat;
    }

    public String getSlbVersion() {
        return slbVersion;
    }

    public void setSlbVersion(String slbVersion) {
        this.slbVersion = slbVersion;
    }

    public String getSlbSignatureMethod() {
        return slbSignatureMethod;
    }

    public void setSlbSignatureMethod(String slbSignatureMethod) {
        this.slbSignatureMethod = slbSignatureMethod;
    }

    public String getSlbSignatureVersion() {
        return slbSignatureVersion;
    }

    public void setSlbSignatureVersion(String slbSignatureVersion) {
        this.slbSignatureVersion = slbSignatureVersion;
    }

    public String getSlbUrl() {
        return slbUrl;
    }

    public void setSlbUrl(String slbUrl) {
        this.slbUrl = slbUrl;
    }

    public String getSlbAction() {
        return slbAction;
    }

    public void setSlbAction(String slbAction) {
        this.slbAction = slbAction;
    }

    public String getSlbLoadBalancerId() {
        return slbLoadBalancerId;
    }

    public void setSlbLoadBalancerId(String slbLoadBalancerId) {
        this.slbLoadBalancerId = slbLoadBalancerId;
    }

    public String getSlbListenerPort() {
        return slbListenerPort;
    }

    public void setSlbListenerPort(String slbListenerPort) {
        this.slbListenerPort = slbListenerPort;
    }

    public String getRedisFormat() {
        return redisFormat;
    }

    public void setRedisFormat(String redisFormat) {
        this.redisFormat = redisFormat;
    }

    public String getRedisVersion() {
        return redisVersion;
    }

    public void setRedisVersion(String redisVersion) {
        this.redisVersion = redisVersion;
    }

    public String getRedisSignatureMethod() {
        return redisSignatureMethod;
    }

    public void setRedisSignatureMethod(String redisSignatureMethod) {
        this.redisSignatureMethod = redisSignatureMethod;
    }

    public String getRedisSignatureVersion() {
        return redisSignatureVersion;
    }

    public void setRedisSignatureVersion(String redisSignatureVersion) {
        this.redisSignatureVersion = redisSignatureVersion;
    }

    public String getRedisUrl() {
        return redisUrl;
    }

    public void setRedisUrl(String redisUrl) {
        this.redisUrl = redisUrl;
    }

    public String getRedisDescribeHistoryMonitorValues() {
        return redisDescribeHistoryMonitorValues;
    }

    public void setRedisDescribeHistoryMonitorValues(String redisDescribeHistoryMonitorValues) {
        this.redisDescribeHistoryMonitorValues = redisDescribeHistoryMonitorValues;
    }

    public String getRedisInstanceId() {
        return redisInstanceId;
    }

    public void setRedisInstanceId(String redisInstanceId) {
        this.redisInstanceId = redisInstanceId;
    }

    public String getRedisIntervalForHistory() {
        return redisIntervalForHistory;
    }

    public void setRedisIntervalForHistory(String redisIntervalForHistory) {
        this.redisIntervalForHistory = redisIntervalForHistory;
    }

}
