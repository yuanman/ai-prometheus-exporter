package com.ai.prometheus.redis;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import com.ai.prometheus.slb.SLB;
import com.ai.prometheus.util.BeanUtils;
import com.ai.prometheus.util.HttpClientFactory;
import com.ai.prometheus.util.SrvConfig;
import com.ai.prometheus.vo.RedisConfig;
import com.aliyun.openservices.ons.api.impl.authority.OnsAuthSigner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Redis {
    private Logger logger = Logger.getLogger(SLB.class);

    @Autowired
    private SrvConfig config;

    public HttpResponse postRequest(RedisConfig redis, String url, String... args) {
        String accesskey = config.getAccesskey(); // 2SbUQlH7FJKyur9V
        String securityKey = config.getSecurityKey(); // WGoQpjLNgTA4VwrHNQBcqe0zDbXZti

        String format = redis.getFormat(); // JSON
        String version = redis.getVersion(); // 2015-01-01
        String signatureMethod = redis.getSignaturemethod(); // HMAC-SHA1
        String signatureVersion = redis.getSignatureversion(); // 1.0

        Map<String, String> paras = new TreeMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        paras.put("AccessKeyId", accesskey);
        paras.put("Format", format);
        paras.put("Version", version);
        paras.put("SignatureMethod", signatureMethod);
        paras.put("SignatureNonce", String.valueOf(System.currentTimeMillis()));
        paras.put("SignatureVersion", signatureVersion);
        paras.put("Timestamp", df.format(new Date()));

        for (String arg : args) {
            String[] kv = arg.split(":", 2);
            paras.put(kv[0], kv[1]);
        }

        String origin = Redis.combineContent(paras);
        String signature = OnsAuthSigner.calSignature(origin, securityKey + "&");
        paras.put("Signature", signature);

        List<NameValuePair> data = new ArrayList<>();
        for (final Map.Entry<String, String> entry : paras.entrySet()) {
            data.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        HttpResponse response;
        try {
            HttpEntity requestEntity = new UrlEncodedFormEntity(data, "UTF-8");
            url = url + "/?" + EntityUtils.toString(requestEntity);
            HttpUriRequest request =
                RequestBuilder.get(url).setConfig(HttpClientFactory.getDefaultRequestConfig()).build();
            response = HttpClientFactory.get().execute(request);
        } catch (Exception e) {
            throw new RuntimeException(
                String.format("Exception occurred when post a http request[%s], msg[%s]", url, e.getMessage()));
        }
        return response;
    }

    public static String combineContent(Map<String, String> params) {
        int isFirst = 0;
        StringBuilder sb = new StringBuilder("");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (isFirst > 0)
                sb.append("&");
            isFirst++;
            sb.append(new BasicNameValuePair(percent_encode(entry.getKey()), percent_encode(entry.getValue())));

        }
        return "GET&%2F&" + percent_encode(sb.toString());
    }

    public static String percent_encode(String encodeStr) {
        String res = "";
        try {
            res = URLEncoder.encode(encodeStr, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 对DescribeHistoryMonitorValues方法返回的数据进行解析， 并设置RedisBean的usedMemory值
     */
    public void parseUsedMemory(RedisBean bean, String json) throws JSONException, Exception {
        org.json.JSONObject object = new org.json.JSONObject(json);
        String monitorHistory = object.getString("MonitorHistory");
        logger.info(monitorHistory);

        com.alibaba.fastjson.JSONArray jarr = com.alibaba.fastjson.JSONArray.parseArray("[" + monitorHistory + "]");
        for (Iterator<Object> iterator = jarr.iterator(); iterator.hasNext();) {
            com.alibaba.fastjson.JSONObject job = (com.alibaba.fastjson.JSONObject)iterator.next();
            Set<String> s = job.keySet();
            for (String string : s) {
                JSONObject temp = JSONObject.parseObject(job.get(string) + "");
                bean.setUsedMemCache(temp.get("UsedMemory") + "");
            }
        }
    }

    /**
     * 对DescribeInstanceAttribute方法返回的数据进行解析， 并设置RedisBean的capacity值
     */
    public void parseCapacity(RedisBean bean, String json) throws JSONException, Exception {
        org.json.JSONObject object = new org.json.JSONObject(json);
        org.json.JSONObject instances = (org.json.JSONObject)object.get("Instances");
        org.json.JSONArray kvStoreInstanceArray = instances.getJSONArray("KVStoreInstance");
        logger.info(kvStoreInstanceArray);

        if (kvStoreInstanceArray.length() > 0) {
            org.json.JSONObject obj = kvStoreInstanceArray.getJSONObject(0);
            bean.setCapacity(obj.get("Capacity") + "");
        }
    }

    /**
     * 获取 Redis 实例的内存数据
     */
    public List<RedisBean> getRedisMetrics() {
        List<RedisBean> rs = new ArrayList<RedisBean>();
        BeanUtils<RedisConfig> beanUtils = new BeanUtils<RedisConfig>();
        try {
            List<RedisConfig> redisList = beanUtils.ListMap2JavaBean(config.getRedisList(), RedisConfig.class);

            /** 根据配置文件中redis，请求获取redis监控数据 **/
            for (int i = 0; i < redisList.size(); i++) {
                RedisConfig redis = redisList.get(i);

                String redisUrl = redis.getUrl();
                String instanceId = "InstanceId:" + redis.getInstanceid();
                String instanceIds = "InstanceIds:" + redis.getInstanceid();
                String actionDescribeInstances = "Action:" + redis.getDescinstances();
                String actionDescribeHistoryMonitorValues = "Action:" + redis.getDeschistorymonitorvalues();
                String intervalForHistory = "IntervalForHistory:" + redis.getInterval();

                RedisBean bean = new RedisBean();
                bean.setInstanceId(redis.getInstanceid());

                // 1.first to get redis capacity.
                HttpResponse response_1 = postRequest(redis, redisUrl, actionDescribeInstances, instanceIds);
                try {
                    String json_1 = EntityUtils.toString(response_1.getEntity());
                    logger.info(json_1);
                    parseCapacity(bean, json_1);
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                    ex.printStackTrace();
                }

                // 2.get usedMemory.
                // 查看redis监控值,注意时区为0时区，时间要减8:00
                HttpResponse response = postRequest(redis, redisUrl, actionDescribeHistoryMonitorValues, instanceId,
                    "StartTime:2018-03-08T08:00:00Z", "EndTime:2018-03-08T08:01:00Z", intervalForHistory);
                try {
                    String json = EntityUtils.toString(response.getEntity());
                    logger.info(json);
                    parseUsedMemory(bean, json);
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                    ex.printStackTrace();
                }

                rs.add(bean);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }

        return rs;
    }

    public static void main(String[] args) {
        // // 查看redis监控值,注意时区为0时区，时间要减8:00
        // HttpResponse response = postRequest("http://kvstore.cpct.com.cn",
        // "Action:DescribeHistoryMonitorValues",
        // "InstanceId:74314bb1fe9c4ecc", "StartTime:2018-03-08T08:00:00Z",
        // "EndTime:2099-03-08T11:01:00Z",
        // "IntervalForHistory:01m");
        //
        // // 查看redis实例的总容量等信息
        // HttpResponse response_1 =
        // postRequest("http://kvstore.cpct.com.cn", "Action:DescribeInstances",
        // "InstanceIds:74314bb1fe9c4ecc");
        //
        // String json;
        // try {
        // json = EntityUtils.toString(response.getEntity());
        // System.out.println(json);
        // } catch (ParseException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }
}
