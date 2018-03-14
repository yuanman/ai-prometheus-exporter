package com.ai.prometheus.edas;

import java.util.List;

public class EDASBean {
    private String appId;
    private String appName;
    private String appState;
    private int instanceAmount;
    private List<EDASInstance> appList;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppState() {
        return appState;
    }

    public void setAppState(String appState) {
        this.appState = appState;
    }

    public int getInstanceAmount() {
        return instanceAmount;
    }

    public void setInstanceAmount(int instanceAmount) {
        this.instanceAmount = instanceAmount;
    }

    public List<EDASInstance> getAppList() {
        return appList;
    }

    public void setAppList(List<EDASInstance> appList) {
        this.appList = appList;
    }
}
