package com.ai.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import com.ai.prometheus.slb.BackendServer;
import com.ai.prometheus.slb.SLB;
import com.ai.prometheus.slb.SLBBean;
import com.ai.prometheus.util.HttpClientFactory;
import com.aliyun.openservices.ons.api.impl.authority.OnsAuthSigner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class SLBTest {
    final static int SLB_STATUS_CODE_NORMAL = 0;
    final static int SLB_STATUS_CODE_ABNORMAL = 1;

    public static HttpResponse postRequest(String url, String... args) {
        Map<String, String> paras = new TreeMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        paras.put("Format", "JSON");
        paras.put("Version", "2014-05-15");
        paras.put("SignatureMethod", "HMAC-SHA1");
        paras.put("SignatureNonce", String.valueOf(System.currentTimeMillis()));
        paras.put("SignatureVersion", "1.0");
        paras.put("AccessKeyId", "2SbUQlH7FJKyur9V");
        paras.put("Timestamp", df.format(new Date()));

        for (String arg : args) {
            String[] kv = arg.split(":", 2);
            paras.put(kv[0], kv[1]);
        }

        String origin = SLB.combineContent(paras);
        String signature = OnsAuthSigner.calSignature(origin, "WGoQpjLNgTA4VwrHNQBcqe0zDbXZti" + "&");
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
     * 调用SLB后端服务器健康检查API，获取SLB服务端口状态。
     * 
     * @return
     */
    public SLBBean getHealthStatus() {
        String slbUrl = "http://slb.cpct.com.cn";
        String action = "Action:DescribeHealthStatus";
        String loadBalancerId = "LoadBalancerId:161fa5394b3-cn-beijing-zgyz-d01";
        String listenPort = "ListenerPort:80";
        HttpResponse response = postRequest(slbUrl, action, loadBalancerId, listenPort);

        SLBBean bean = new SLBBean();
        bean.setListenerPort(listenPort);
        bean.setLoadBalancerId(loadBalancerId);
        bean.setSlbStatusCode(SLB_STATUS_CODE_NORMAL);
        try {
            String json = EntityUtils.toString(response.getEntity());
            parse(bean, json);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bean;
    }

    /**
     * parse SLB API DescribeHealthStatus response.
     * 
     * @param bean
     * @param json
     * @return
     * @throws Exception
     */
    public SLBBean parse(SLBBean bean, String json) throws Exception {
        System.out.println("++++++++ postRequest return:" + json);
        JSONObject object = new JSONObject(json);
        JSONObject backendServers = (JSONObject)object.get("BackendServers");
        JSONArray jsonArray = backendServers.getJSONArray("BackendServer");
        System.out.println("++++++++ parse response +++++++++ BackendServer:" + jsonArray);

        List<BackendServer> details = new ArrayList<BackendServer>();
        for (int i = 0; i < jsonArray.length(); i++) {
            BackendServer vo = new BackendServer();
            JSONObject obj = jsonArray.getJSONObject(i);
            if ("abnormal".equals((String)obj.get("ServerHealthStatus"))) {
                bean.setSlbStatusCode(SLB_STATUS_CODE_ABNORMAL);
            }
            vo.setServerId((String)obj.get("ServerId"));
            vo.setServerHealthStatus((String)obj.get("ServerHealthStatus"));
            details.add(vo);
        }

        bean.setServerList(details);
        return bean;
    }

    public static void main(String[] args) {
        SLBTest action = new SLBTest();
        // 查看SLB后端服务器健康检查
        SLBBean vo = action.getHealthStatus();
        if (vo.getServerList().size() > 0) {
            System.out.println("++++++++ return balanceId" + vo.getLoadBalancerId());
            System.out.println("++++++++ return serviceId:" + vo.getServerList().get(0).getServerId());
            System.out.println("++++++++ return serviceStatus:" + vo.getServerList().get(0).getServerHealthStatus());
            System.out.println("++++++++ return statusCode:" + vo.getSlbStatusCode());
        }

        // HttpResponse response = postRequest("http://slb.cpct.com.cn",
        // "Action:DescribeHealthStatus",
        // "LoadBalancerId:161fa5394b3-cn-beijing-zgyz-d01", "ListenerPort:80");
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

        // 查看SLB端口状态-TCP
        // HttpResponse response=postRequest("http://slb.cpct.com.cn",
        // "Action:DescribeLoadBalancerTCPListenerAttribute",
        // "LoadBalancerId:161fa5394b3-cn-beijing-zgyz-d01",
        // "ListenerPort:80");

        // 查看SLB端口状态-HTTP
        // HttpResponse response=postRequest("http://slb.cpct.com.cn",
        // "Action:DescribeLoadBalancerHTTPListenerAttribute",
        // "LoadBalancerId:16078489fb1-cn-beijing-zgyz-d01",
        // "ListenerPort:80");

        // 查看SLB端口状态-HTTPS
        // HttpResponse response=postRequest("http://slb.cpct.com.cn",
        // "Action:DescribeLoadBalancerHTTPSListenerAttribute",
        // "LoadBalancerId:15b8fb7f8a3-cn-beijing-zgyz-d01",
        // "ListenerPort:443");
    }
}
