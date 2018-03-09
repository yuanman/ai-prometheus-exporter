package com.asiainfo.prometheus.slb;

public class BackendServer {
    public String serverId;
    public String serverHealthStatus;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerHealthStatus() {
        return serverHealthStatus;
    }

    public void setServerHealthStatus(String serverHealthStatus) {
        this.serverHealthStatus = serverHealthStatus;
    }

}
