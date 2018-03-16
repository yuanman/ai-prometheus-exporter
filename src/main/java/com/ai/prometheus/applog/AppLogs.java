package com.ai.prometheus.applog;

import java.util.ArrayList;
import java.util.List;

import com.ai.prometheus.util.HttpClientFactory;
import com.ai.prometheus.util.SrvConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppLogs {
    private static Logger logger = Logger.getLogger(AppLogs.class);

    @Autowired
    private SrvConfig config;

    /**
     * 请求elasticsearch，获取应用异常日志的统计结果。
     */
    public List<LogBean> getAppLogs() {
        String url = config.getEdasLogUrl() + config.getEdasLogIndex() + "/_search";
        String logTimeRange = config.getEdasLogTimeRange();
        String logMessageKey = config.getEdasLogMessageKey();
        String logLevel = config.getEdasLogLevel();
        
        List<LogBean> list = new ArrayList<LogBean>();

        /** 拼装调用es接口获取应用异常日志的聚合语句。 **/
        String data ="{\"query\": "
            + "{\"bool\": "
            + "{\"filter\": {\"range\": {\"time\": {\"gte\": \"now-"+logTimeRange+"\",\"lte\": \"now\"}}}, "
            + "\"must\": ["
            + "{ \"match\": {\"message\": \""+logMessageKey+"\"}},"
            + "{ \"match\": {\"level\": \""+logLevel+"\"}}"
            + "]}},"
            + "\"aggs\": {\"exceptions\" : "
            + "{\"terms\": {\"field\": \"_type\"},"
            + "\"aggs\": {\"exceptionList\": "
            + "{\"terms\": {\"field\": \"ip\" }}}}},"
            + "\"size\": 0 }";
        
        logger.info(data);
        
        try {
            StringEntity requestEntity = new StringEntity(data, "UTF-8");
            requestEntity.setContentType("application/json");
            HttpUriRequest request = RequestBuilder.post(url).setConfig(HttpClientFactory.getDefaultRequestConfig())
                .setEntity(requestEntity).build();

            HttpResponse response = HttpClientFactory.get().execute(request);
            String rsp = EntityUtils.toString(response.getEntity());
            logger.info(rsp);
            parse(rsp, list);
        } catch (Exception ex) {
            logger.error("request elasticsearch " + url + " error. [" + ex.getMessage() + "]");
            ex.printStackTrace();
        }

        return list;
    }

    /**
     * 解析elasticsearch返回的异常日志数据json统计结果。
     * @param rsp
     * @param list
     * @return
     */
    private static void parse(String rsp, List<LogBean> list) {
        try {
            JSONObject object = new JSONObject(rsp);
            JSONObject aggsObj = object.getJSONObject("aggregations"); 
            JSONObject excptionObj = aggsObj.getJSONObject("exceptions"); 
            logger.info("====exceptions：" + excptionObj);
            
            /** 最外层桶，按jt/yw的type进行聚合统计 **/
            JSONArray bucketsArray = excptionObj.getJSONArray("buckets");
            logger.info("====最外层桶 buckets:" + bucketsArray);
            for (int i = 0; i < bucketsArray.length(); i++) {
                
                JSONObject typeObj = bucketsArray.getJSONObject(i);
                String indexType = (String)typeObj.get("key");  //获取最外层桶的索引type名称。
                JSONObject exceptionListObj = typeObj.getJSONObject("exceptionList"); 
                
                /** 里面的桶，按ip进行聚合统计 **/
                JSONArray ipBucketsArray = exceptionListObj.getJSONArray("buckets");
                for (int j = 0; j < ipBucketsArray.length(); j++) {
                    JSONObject ipObj = ipBucketsArray.getJSONObject(j);
                    
                    LogBean logBean = new LogBean();
                    logBean.setIndex(indexType);
                    logBean.setIp((String)ipObj.get("key"));
                    logBean.setExceptionAmout((Integer)ipObj.get("doc_count"));
                    
                    list.add(logBean);
                }
            }
        } catch (Exception ex) {
            logger.error("==== parse elasticsearch return data error. [" + ex.getMessage() + "]");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        /** 调用es接口，获取应用异常日志 **/
        String url = "http://192.168.248.173:9200/edas*/_search";

        /** 拼装调用es接口获取应用异常日志的聚合语句。 **/
        String data ="{\"query\": "
            + "{\"bool\": "
            + "{\"filter\": {\"range\": {\"time\": {\"gte\": \"now-5d\",\"lte\": \"now\"}}}, "
            + "\"must\": ["
            + "{ \"match\": {\"message\": \"exception\"}},"
            + "{ \"match\": {\"level\": \"ERROR\"}}"
            + "]}},"
            + "\"aggs\": {\"exceptions\" : "
            + "{\"terms\": {\"field\": \"_type\"},"
            + "\"aggs\": {\"exceptionList\": "
            + "{\"terms\": {\"field\": \"ip\" }}}}},"
            + "\"size\": 0 }";
        
        try {
            StringEntity requestEntity = new StringEntity(data, "UTF-8");
            requestEntity.setContentType("application/json");
            HttpUriRequest request = RequestBuilder.post(url).setConfig(HttpClientFactory.getDefaultRequestConfig())
                .setEntity(requestEntity).build();
            
            HttpResponse response = HttpClientFactory.get().execute(request);
            String rsp = EntityUtils.toString(response.getEntity());
            logger.info(rsp);
            
            List<LogBean> list = new ArrayList<LogBean>();
            parse(rsp, list);
            for(LogBean bean:list){
                logger.info("====采集到到日志Metrics："+bean.getIndex()+"|"+bean.getIp()+"|"+bean.getExceptionAmout());
            }
        } catch (Exception e) {
            throw new RuntimeException(
                String.format("Exception occurred when post a http request[%s], msg[%s]", url, e.getMessage()));
        }
    }
}
