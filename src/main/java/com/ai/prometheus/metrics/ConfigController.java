package com.ai.prometheus.metrics;

import java.util.List;

import com.ai.prometheus.util.BeanUtils;
import com.ai.prometheus.util.RdsConfig;
import com.ai.prometheus.util.SrvConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "config")
public class ConfigController {
    private Logger logger = Logger.getLogger(ConfigController.class);
    
    @Autowired
    private SrvConfig config;

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "acceskey: " + config.getAccesskey();
    }

    @RequestMapping(value = "rds", method = RequestMethod.GET)
    public String rds() throws Exception {
        logger.debug(config.getRdsList());
        BeanUtils<RdsConfig> beanUtils = new BeanUtils<RdsConfig>();
        logger.debug(beanUtils.ListMap2JavaBean(config.getRdsList(), RdsConfig.class).toString());

        List<RdsConfig> rdsList = beanUtils.ListMap2JavaBean(config.getRdsList(), RdsConfig.class);
        for(int i=0;i<rdsList.size();i++){
            logger.info("++++["+i+"]:" + rdsList.get(i).getFormat());
            logger.info("++++["+i+"]:" + rdsList.get(i).getSignaturemethod());
            logger.info("++++["+i+"]:" + rdsList.get(i).getSignatureversion());
            logger.info("++++["+i+"]:" + rdsList.get(i).getVersion());
            logger.info("++++["+i+"]:" + rdsList.get(i).getUrl());
        }
        
//        for (Map<String, Object> m : config.getRdsList()) {
//            for (String k : m.keySet()) {
//                logger.debug(k + " : " + m.get(k));
//            }
//        }
        
        return "acceskey: " + config.getAccesskey();
    }
}
