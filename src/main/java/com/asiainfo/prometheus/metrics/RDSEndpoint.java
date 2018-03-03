package com.asiainfo.prometheus.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import org.springframework.stereotype.Component;

@Component
public class RDSEndpoint extends Collector {
    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<>();

        // 创建metrics指标
        GaugeMetricFamily labeledGauge = new GaugeMetricFamily("rds_custom_metrics", "custom metrics",
            Collections.singletonList("labelname"));

        // 设置指标的label以及value
        labeledGauge.addMetric(Collections.singletonList("labelvalue"), 1);

        mfs.add(labeledGauge);
        return mfs;
    }
}
