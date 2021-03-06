package com.ai.prometheus.vo;

public class SlbConfig {
    private String format;
    private String version;
    private String signaturemethod;
    private String signatureversion;
    private String url;
    private String action;
    private String loadbalancerid;
    private String listenerport;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSignaturemethod() {
        return signaturemethod;
    }

    public void setSignaturemethod(String signaturemethod) {
        this.signaturemethod = signaturemethod;
    }

    public String getSignatureversion() {
        return signatureversion;
    }

    public void setSignatureversion(String signatureversion) {
        this.signatureversion = signatureversion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLoadbalancerid() {
        return loadbalancerid;
    }

    public void setLoadbalancerid(String loadbalancerid) {
        this.loadbalancerid = loadbalancerid;
    }

    public String getListenerport() {
        return listenerport;
    }

    public void setListenerport(String listenerport) {
        this.listenerport = listenerport;
    }

}
