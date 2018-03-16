package com.ai.prometheus.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @Autowired
    private AppLogEndpoint appLogExporter;
    
    @Autowired
    private MQEndpoint mqExporter;
    
    @Autowired
    private RDSEndpoint rdsExporter;
    
    @Autowired  
    private SLBEndpoint slbExporter;
    
    @Autowired
    private RedisEndpoint redisExporter;
    
    @Autowired
    private EDASEndpoint edasExporter;
    
    @RequestMapping("/register")
    public void rdsExporter() {
        appLogExporter.register();
        mqExporter.register();
        rdsExporter.register();
        slbExporter.register();
        redisExporter.register();
        edasExporter.register();
    }
}
