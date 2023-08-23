package com.atguigu.boot3.actuator.health;

import com.atguigu.boot3.actuator.component.MyHahaComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * 1. 实现HealthIndicator接口
 * 2. 继承
 */
@Component
//public class MyHahaHealthIndicator implements HealthIndicator {
//    @Override
//    public Health health() {
//        return null;
//    }
//}

public class MyHahaHealthIndicator extends AbstractHealthIndicator {

    @Autowired
    MyHahaComponent myHahaComponent;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        int check = myHahaComponent.check();
        if (check == 1) {
            builder.up().withDetail("code", "100").withDetail("msg", "健康").build();
        } else {
            builder.down().withDetail("msg", "服务异常").build();
        }
    }
}
