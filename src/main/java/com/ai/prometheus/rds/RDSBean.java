package com.ai.prometheus.rds;

import java.util.List;

public class RDSBean {
    public String dbInstanceId;
    public String dbName;
    public int rdsShowCount;
    public String startTime;
    public String endTime;
    public List<RDSItemDetail> detailList;
    
    public String getDbInstanceId() {
        return dbInstanceId;
    }
    public void setDbInstanceId(String dbInstanceId) {
        this.dbInstanceId = dbInstanceId;
    }
    public String getDbName() {
        return dbName;
    }
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
    public int getRdsShowCount() {
        return rdsShowCount;
    }
    public void setRdsShowCount(int rdsShowCount) {
        this.rdsShowCount = rdsShowCount;
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
    public List<RDSItemDetail> getDetailList() {
        return detailList;
    }
    public void setDetailList(List<RDSItemDetail> detailList) {
        this.detailList = detailList;
    }
}
