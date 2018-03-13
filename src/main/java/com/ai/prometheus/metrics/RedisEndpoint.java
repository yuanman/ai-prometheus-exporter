package com.ai.prometheus.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ai.prometheus.redis.Redis;
import com.ai.prometheus.redis.RedisBean;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisEndpoint extends Collector {

    @Autowired
    private Redis redisSrv;

    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();

        GaugeMetricFamily metricFamily = new GaugeMetricFamily("redis_metric_server_capacity",
            "redis_metric_server_capacity", Collections.singletonList("instanceid"));
        List<RedisBean> list = redisSrv.getRedisMetrics();
        for (RedisBean bean : list) {
            metricFamily.addMetric(Collections.singletonList(bean.getInstanceId()),
                Double.parseDouble(bean.getCapacity()));
        }

        mfs.add(metricFamily);

        GaugeMetricFamily metricUsedMem = new GaugeMetricFamily("redis_metric_server_usedMemory",
            "redis_metric_server_usedMemory", Collections.singletonList("instanceid"));
        for (RedisBean bean : list) {
            metricUsedMem.addMetric(Collections.singletonList(bean.getInstanceId()),
                Double.parseDouble(bean.getUsedMemCache())/1024/1024);
        }

        mfs.add(metricUsedMem);

        return mfs;
    }

}
