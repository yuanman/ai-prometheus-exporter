package com.asiainfo.prometheus.slb;

import java.util.List;

public class SLBBean {
    public String loadBalancerId;
    public String listenerPort;
    public String startTime;
    public String endTime;
    public int slbStatusCode;
    public List<BackendServer> serverList;

    public String getLoadBalancerId() {
        return loadBalancerId;
    }

    public void setLoadBalancerId(String loadBalancerId) {
        this.loadBalancerId = loadBalancerId;
    }

    public String getListenerPort() {
        return listenerPort;
    }

    public void setListenerPort(String listenerPort) {
        this.listenerPort = listenerPort;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getSlbStatusCode() {
        return slbStatusCode;
    }

    public void setSlbStatusCode(int slbStatusCode) {
        this.slbStatusCode = slbStatusCode;
    }

    public List<BackendServer> getServerList() {
        return serverList;
    }

    public void setServerList(List<BackendServer> serverList) {
        this.serverList = serverList;
    }

}
