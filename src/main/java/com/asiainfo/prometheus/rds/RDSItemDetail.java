package com.asiainfo.prometheus.rds;

public class RDSItemDetail {
    public String hostAddress;
    public String queryTimes;
    public String lockTimes;
    public String sqlText;
    
    public String getHostAddress() {
        return hostAddress;
    }
    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }
    public String getQueryTimes() {
        return queryTimes;
    }
    public void setQueryTimes(String queryTimes) {
        this.queryTimes = queryTimes;
    }
    public String getLockTimes() {
        return lockTimes;
    }
    public void setLockTimes(String lockTimes) {
        this.lockTimes = lockTimes;
    }
    public String getSqlText() {
        return sqlText;
    }
    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }
}
