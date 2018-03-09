package com.asiainfo.prometheus.redis;

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

import com.aliyun.openservices.ons.api.impl.authority.OnsAuthSigner;
import com.asiainfo.prometheus.util.HttpClientFactory;
import com.asiainfo.prometheus.util.SrvConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class Redis {

    @Autowired
    private SrvConfig config;

    public HttpResponse postRequest(String url, String... args) {
        String accesskey = config.getAccesskey(); // 2SbUQlH7FJKyur9V
        String securityKey = config.getSecurityKey(); // WGoQpjLNgTA4VwrHNQBcqe0zDbXZti
        String format = config.getRdsFormat(); // JSON
        String version = config.getRdsVersion(); // 2015-01-01
        String signatureMethod = config.getRdsSignatureMethod(); // HMAC-SHA1
        String signatureVersion = config.getRdsSignatureVersion(); // 1.0

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
     * 
     * @param bean
     * @param json
     * @throws JSONException
     * @throws Exception
     */
    public void parseUsedMemory(RedisBean bean, String json) throws JSONException, Exception {
        System.out.println(json);

        org.json.JSONObject object = new org.json.JSONObject(json);
        String monitorHistory = object.getString("MonitorHistory");
        System.out.println(monitorHistory);

        com.alibaba.fastjson.JSONArray jarr = com.alibaba.fastjson.JSONArray.parseArray("[" + monitorHistory + "]");
        for (Iterator<Object> iterator = jarr.iterator(); iterator.hasNext();) {
            com.alibaba.fastjson.JSONObject job = (com.alibaba.fastjson.JSONObject)iterator.next();
            Set<String> s = job.keySet();
            for (String string : s) {
                System.out.println(job.get(string));
                JSONObject temp = JSONObject.parseObject(job.get(string) + "");
                System.out.println(temp.get("UsedMemory"));
                bean.setUsedMemCache(temp.get("UsedMemory") + "");
            }
        }
    }

    /**
     * 对DescribeInstanceAttribute方法返回的数据进行解析， 并设置RedisBean的capacity值
     * 
     * @param bean
     * @param json
     * @throws JSONException
     * @throws Exception
     */
    public void parseCapacity(RedisBean bean, String json) throws JSONException, Exception {
        org.json.JSONObject object = new org.json.JSONObject(json);
        org.json.JSONObject instances = (org.json.JSONObject)object.get("Instances");
        org.json.JSONArray kvStoreInstanceArray = instances.getJSONArray("KVStoreInstance");
        System.out.println(kvStoreInstanceArray);

        if (kvStoreInstanceArray.length() > 0) {
            org.json.JSONObject obj = kvStoreInstanceArray.getJSONObject(0);
            bean.setCapacity(obj.get("Capacity") + "");
        }
    }

    public RedisBean getRedisMetrics() {
        String redisUrl = config.getRedisUrl();
        String instanceId = "InstanceId:" + config.getRedisInstanceId();
        String instanceIds = "InstanceIds:" + config.getRedisInstanceId();
        String actionDescribeInstances = "Action:" + config.getRedisDescribeInstances();
        String actionDescribeHistoryMonitorValues = "Action:" + config.getRedisDescribeHistoryMonitorValues();
        String intervalForHistory = "IntervalForHistory:" + config.getRedisIntervalForHistory();

        // 1.first to get redis capacity.
        HttpResponse response_1 = postRequest(redisUrl, actionDescribeInstances, instanceIds);

        RedisBean bean = new RedisBean();
        try {
            String json_1 = EntityUtils.toString(response_1.getEntity());
            System.out.println(json_1);
            parseCapacity(bean, json_1);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2.get usedMemory.
        // 查看redis监控值,注意时区为0时区，时间要减8:00
        HttpResponse response = postRequest(redisUrl, actionDescribeHistoryMonitorValues, instanceId,
            "StartTime:2018-03-08T08:00:00Z", "EndTime:2099-03-08T11:01:00Z", intervalForHistory);
        try {
            String json = EntityUtils.toString(response.getEntity());
            System.out.println(json);

            parseUsedMemory(bean, json);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bean;
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
