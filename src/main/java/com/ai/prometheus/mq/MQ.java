package com.ai.prometheus.mq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ai.prometheus.util.HttpClientFactory;
import com.ai.prometheus.util.SrvConfig;
import com.aliyun.openservices.ons.api.impl.authority.OnsAuthSigner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQ {
    
    @Autowired
    private SrvConfig config;

    public HttpResponse postRequest(String url, String... args) {
        String accesskey = config.getAccesskey(); //2SbUQlH7FJKyur9V
        String platform = config.getMqPlatform();   //onsConsole
        String securityKey = config.getSecurityKey(); //WGoQpjLNgTA4VwrHNQBcqe0zDbXZti
        
        Map<String, String> paras = new TreeMap<>();
        paras.put("_accesskey", accesskey);
        paras.put("_platform", platform);
        paras.put("__preventCache", String.valueOf(System.currentTimeMillis()));

        for (String arg : args) {
            String[] kv = arg.split(":");
            paras.put(kv[0], kv[1]);
        }

        String origin = MQ.combineContent(paras);
        String signature = OnsAuthSigner.calSignature(origin, securityKey);
        paras.put("_signature", signature);

        List<NameValuePair> data = new ArrayList<>();
        for (final Map.Entry<String, String> entry : paras.entrySet()) {
            data.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        HttpResponse response;
        try {
            HttpEntity requestEntity = new UrlEncodedFormEntity(data, "UTF-8");
            HttpUriRequest request = RequestBuilder.post(url).setConfig(HttpClientFactory.getDefaultRequestConfig())
                .setEntity(requestEntity).build();
            response = HttpClientFactory.get().execute(request);
        } catch (Exception e) {
            throw new RuntimeException(
                String.format("Exception occurred when post a http request[%s], msg[%s]", url, e.getMessage()));
        }
        return response;
    }

    public static String combineContent(Map<String, String> params) {
        StringBuilder sb = new StringBuilder("");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getValue());
        }
        return sb.toString();
    }

    public List<MQBean> getTotalDiff() {
        String serviceUrl = config.getMqUrl();  
        String regionId = config.getMqRegionId();   
        String consumerId = config.getMqConsumerId();  
        String hasDetail = config.getMqTopicDetail();  
        HttpResponse response = postRequest(serviceUrl,
            "_regionId:"+regionId, "consumerId:"+consumerId, "detail:"+hasDetail);
        
        List<MQBean> list = new ArrayList<MQBean>();
        try {
            String json = EntityUtils.toString(response.getEntity());
//            System.out.println(json);
            JSONObject object = new JSONObject(json);
            JSONObject data = (JSONObject) object.get("data");
            JSONArray jsonArray = data.getJSONArray("detailInTopicList");
//            System.out.println(jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                System.out.println("++++ topic is : " + obj.get("topic") +
                    "  ++++ totalDiff is : " + obj.get("totalDiff"));
                
                MQBean bean = new MQBean();
                bean.setTopic((String)obj.get("topic"));
                bean.setTotalDiff(Integer.parseInt(obj.get("totalDiff")+""));
                
                list.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        return list;
    }
    
    public static void main(String[] args) {
//        HttpResponse response = postRequest("http://mq.console.cpct.com.cn/json/consumer/accumulate",
//            "_regionId:shihua", "consumerId:CID_CRM_TEST_1206", "detail:true");
//
//        try {
//            String json = EntityUtils.toString(response.getEntity());
//            System.out.println(json);
//            
//            JSONObject object = new JSONObject(json);
//            JSONObject data = (JSONObject) object.get("data");
//            JSONArray jsonArray = data.getJSONArray("detailInTopicList");
//            System.out.println(jsonArray);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject obj = jsonArray.getJSONObject(i);
//                System.out.println(obj.toString());
//                System.out.println("++++++++ topic is : " + obj.get("topic"));
//                System.out.println("++++++++ totalDiff is : " + obj.get("totalDiff"));
//            }
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

    }
}