package com.ai.prometheus.metrics;

import com.ai.prometheus.util.SrvConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "config")
public class ConfigController {

    @Autowired
    private SrvConfig config;
    
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "acceskey: " + config.getAccesskey();
    }
}
