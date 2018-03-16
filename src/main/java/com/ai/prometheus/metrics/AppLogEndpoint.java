package com.ai.prometheus.metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ai.prometheus.applog.AppLogs;
import com.ai.prometheus.applog.LogBean;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppLogEndpoint extends Collector {

    @Autowired
    private AppLogs logSrv;

    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        GaugeMetricFamily metricFamily = new GaugeMetricFamily("applog_metric_exception_count",
            "applog_metric_exception_count", Arrays.asList("index", "ip"));

        List<LogBean> list = logSrv.getAppLogs();
        for (LogBean bean : list) {
            metricFamily.addMetric(Arrays.asList(bean.getIndex(), bean.getIp()), bean.getExceptionAmout());
        }

        mfs.add(metricFamily);
        return mfs;
    }

}
