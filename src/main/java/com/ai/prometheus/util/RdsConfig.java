package com.ai.prometheus.util;

public class RdsConfig {
    private String format;
    private String version;
    private String signaturemethod;
    private String signatureversion;
    private String url;
    private String action;
    private String dbinstanceid;
    private String dbname;

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

    public String getDbinstanceid() {
        return dbinstanceid;
    }

    public void setDbinstanceid(String dbinstanceid) {
        this.dbinstanceid = dbinstanceid;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

}