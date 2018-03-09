package com.asiainfo.prometheus.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.asiainfo.prometheus.slb.SLB;
import com.asiainfo.prometheus.slb.SLBBean;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SLBEndpoint extends Collector {

    @Autowired
    private SLB slbSrv;

    public List<MetricFamilySamples> collect() {
        SLBBean bean = slbSrv.getHealthStatus();
        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        GaugeMetricFamily metricFamily = new GaugeMetricFamily("slb_metric_server_health_status",
            "slb_metric_server_health_status", Collections.singletonList("loadBalancerId"));

        // metricFamily.addMetric(Collections.singletonList("instance-111"),
        // NANOSECONDS_PER_SECOND);
        metricFamily.addMetric(Collections.singletonList(bean.getLoadBalancerId()), bean.slbStatusCode);
        mfs.add(metricFamily);
        return mfs;
    }

}
