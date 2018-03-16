package com.ai.prometheus.metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ai.prometheus.edas.EDAS;
import com.ai.prometheus.edas.EDASBean;
import com.ai.prometheus.edas.EDASInstance;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EDASEndpoint extends Collector {

    @Autowired
    private EDAS edasSrv;

    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        GaugeMetricFamily metricFamily = new GaugeMetricFamily("edas_metric_server_health_status",
            "edas_metric_server_health_status", Arrays.asList("appid","appname","appstate","instanceamount","insname","insip"));
        
        List<EDASBean>  list = edasSrv.getEdasAppInfo();
        for (EDASBean bean : list) {
            String appId = bean.getAppId();
            String appName = bean.getAppName();
            String appState = bean.getAppState();
            String insAmount = bean.getInstanceAmount() + "";
            for (EDASInstance ins : bean.getAppList()) {
                metricFamily.addMetric(
                    Arrays.asList(appId, appName, appState, insAmount, ins.getInstanceName(), ins.getIpAddr()),
                    Double.parseDouble(ins.getState()));
            }
        }
        
        mfs.add(metricFamily);
        return mfs;
    }

}
