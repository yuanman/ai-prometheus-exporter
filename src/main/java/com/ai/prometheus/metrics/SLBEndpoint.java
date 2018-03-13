package com.ai.prometheus.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ai.prometheus.slb.SLB;
import com.ai.prometheus.slb.SLBBean;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SLBEndpoint extends Collector {

    @Autowired
    private SLB slbSrv;

    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        GaugeMetricFamily metricFamily = new GaugeMetricFamily("slb_metric_server_health_status",
            "slb_metric_server_health_status", Collections.singletonList("loadBalancerId"));

        List<SLBBean>  list = slbSrv.getHealthStatus();
        for (SLBBean bean : list) {
            metricFamily.addMetric(Collections.singletonList(bean.getLoadBalancerId()), bean.slbStatusCode);
        }
        
        mfs.add(metricFamily);
        return mfs;
    }

}
