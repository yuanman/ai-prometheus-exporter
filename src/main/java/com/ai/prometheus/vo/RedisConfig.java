package com.ai.prometheus.vo;

public class RedisConfig {
    private String format;
    private String version;
    private String signaturemethod;
    private String signatureversion;
    private String url;

    private String descinstances;
    private String deschistorymonitorvalues;
    private String instanceid;
    private String interval;

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

    public String getDescinstances() {
        return descinstances;
    }

    public void setDescinstances(String descinstances) {
        this.descinstances = descinstances;
    }

    public String getDeschistorymonitorvalues() {
        return deschistorymonitorvalues;
    }

    public void setDeschistorymonitorvalues(String deschistorymonitorvalues) {
        this.deschistorymonitorvalues = deschistorymonitorvalues;
    }

    public String getInstanceid() {
        return instanceid;
    }

    public void setInstanceid(String instanceid) {
        this.instanceid = instanceid;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

}
