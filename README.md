# 概述

使用了 Spring Boot 构建的 Prometheus exporter，用于收集PaaS服务组件的监控数据。

运行命令:
`java -jar build/libs/ai-prometheus-exporter-0.1.0.jar`

应用运行在 <http://localhost:8080/>,
管理接口运行在 <http://localhost:8081/>, 
Prometheus endpoint 运行在 <http://localhost:8081/prometheus-metrics/>.

**注意:** `management.security.enabled` 需要设置为 `false`.

配置文件：
application.yml
