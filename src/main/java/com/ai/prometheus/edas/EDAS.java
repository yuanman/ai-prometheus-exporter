package com.ai.prometheus.edas;

import java.util.ArrayList;
import java.util.List;

import com.ai.prometheus.util.SrvConfig;
import com.aliyun.edas.open.api.EdasApiClient;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EDAS {
    private static Logger logger = Logger.getLogger(EDAS.class);

    @Autowired
    private SrvConfig config;

    public List<EDASBean> getEdasAppInfo() {
        String accesskey = config.getAccesskey(); // 2SbUQlH7FJKyur9V
        String securityKey = config.getSecurityKey(); // WGoQpjLNgTA4VwrHNQBcqe0zDbXZti
        String url = config.getEdasUrl();
        String action = config.getEdasAction();
        List<EDASBean> list = new ArrayList<EDASBean>();
        
        try {
            /** 获取应用列表 **/
            EdasApiClient edasApiClient = new EdasApiClient(url, accesskey, securityKey);
            String rsp = edasApiClient.callApi(action, null);
            logger.info(rsp);
            parseAppList(rsp, list);
        } catch (Exception ex) {
            logger.error("request edas " + action + " error. [" + ex.getMessage() + "]");
            ex.printStackTrace();
        }

        return list;
    }

//    public List<EDASBean> countEdasException() {
//        String url = config.getEdasLogUrl();
//        String logIndex = config.getEdasLogIndex();
//        String logTimeRange = config.getEdasLogTimeRange();
//        String logMessageKey = config.getEdasLogMessageKey();
//        String logLevel = config.getEdasLogLevel();
//
//        try {
//            /** 获取log中exception的统计数据 **/
////            String rsp = edasApiClient.callApi("/app/app_list", null);
//            logger.info(rsp);
//
//            parse(rsp, list);
//        } catch (Exception ex) {
//            logger.error("request edas /app/app_list error. [" + ex.getMessage() + "]");
//            ex.printStackTrace();
//        }
//
//        return list;
//    }
    
    public static List<EDASBean> parseAppList(String rsp, List<EDASBean> list) {
        try {
            JSONObject object = new JSONObject(rsp);
            JSONArray dataArray = object.getJSONArray("data");
            logger.info(dataArray);
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject app = dataArray.getJSONObject(i);
                EDASBean appBean = new EDASBean();
                appBean.setAppId((String)app.get("appId"));
                appBean.setAppName((String)app.get("name"));
                appBean.setAppState((String)app.get("state"));
                appBean.setInstanceAmount((Integer)app.get("instances"));
                
                JSONArray jsonArray = app.getJSONArray("ecus");
                //logger.info(jsonArray);
                List<EDASInstance> instanceList = new ArrayList<EDASInstance>();
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject obj = jsonArray.getJSONObject(j);
                    EDASInstance bean = new EDASInstance();
                    bean.setAppId((String)app.get("appId"));
                    bean.setInstanceName((String)obj.get("name"));
                    bean.setIpAddr((String)obj.get("ipAddr"));
                    bean.setState((String)obj.get("nodeState"));
                    instanceList.add(bean);
                }
                appBean.setAppList(instanceList);
                
                list.add(appBean);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }

        return list;
    }

    public static void main(String[] args) {
        EdasApiClient edasApiClient = new EdasApiClient("http://edas.console.cpct.com.cn/api", "2SbUQlH7FJKyur9V",
            "WGoQpjLNgTA4VwrHNQBcqe0zDbXZti");
        try {
            String rsp = "-1";
            // 获取应用列表
            rsp = edasApiClient.callApi("/app/app_list", null);
            List<EDASBean> list = new ArrayList<EDASBean>();
            parseAppList(rsp, list);
            // 获取应用信息
//            Map<String, String> params = new HashMap<String, String>();
//            params.put("AppId", "0e129cc4-32bd-48ff-8ba8-e14803049486");
//            resp = edasApiClient.callApi("/app/app_info", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
