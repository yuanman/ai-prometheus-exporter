package com.ai.prometheus.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ai.prometheus.rds.RDS;
import com.ai.prometheus.rds.RDSBean;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RDSEndpoint extends Collector {

    @Autowired
    private RDS rdsSrv;
    
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        GaugeMetricFamily metricFamily =
            new GaugeMetricFamily("rds_metric_slow_qry", "rds_metric_slow_qry", Collections.singletonList("dbInstance"));
        
        List<RDSBean> list = rdsSrv.getSlowCount();
        for (RDSBean bean : list) {
            metricFamily.addMetric(Collections.singletonList(bean.getDbInstanceId()), bean.getRdsShowCount());
        }
        
        mfs.add(metricFamily);
        return mfs;
    }
}
