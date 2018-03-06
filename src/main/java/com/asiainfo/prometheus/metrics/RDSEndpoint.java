package com.asiainfo.prometheus.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import org.springframework.stereotype.Component;

@Component
public class RDSEndpoint extends Collector {

    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        GaugeMetricFamily metricFamily =
            new GaugeMetricFamily("rds_metric_slow_qry", "active_connections", Collections.singletonList("pool"));
        
        metricFamily.addMetric(Collections.singletonList("rds_test_slow_qry"), NANOSECONDS_PER_SECOND);
        mfs.add(metricFamily);
        return mfs;
    }

}
