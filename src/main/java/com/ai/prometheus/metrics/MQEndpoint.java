package com.ai.prometheus.metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ai.prometheus.mq.MQ;
import com.ai.prometheus.mq.MQBean;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQEndpoint extends Collector {
    
    @Autowired
    private MQ mqSrv;
    
    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<>();

        // 创建metrics指标
        GaugeMetricFamily labeledGauge = new GaugeMetricFamily("mq_topic_metrics_diff_total",
            "mq_topic_metrics_diff_total", Arrays.asList("topic_name"));

        // 获取consumerID订阅的所有topic的堆积数据。
        List<MQBean> mqDetails = mqSrv.getTotalDiff();

        for (int i = 0; i < mqDetails.size(); i++) {
            MQBean value = mqDetails.get(i);
            // 设置指标的label以及value
            labeledGauge.addMetric(Collections.singletonList(value.getTopic()), value.getTotalDiff());
        }

        mfs.add(labeledGauge);
        return mfs;
    }
}
