package com.ai.prometheus.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @Autowired
    private MQEndpoint mqExporter;
    
    @Autowired
    private RDSEndpoint rdsExporter;
    
    @RequestMapping("/register")
    public void rdsExporter() {
        mqExporter.register();
        rdsExporter.register();
    }
}
