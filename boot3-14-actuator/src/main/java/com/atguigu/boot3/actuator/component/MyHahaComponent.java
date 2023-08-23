package com.atguigu.boot3.actuator.component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MyHahaComponent {


    Counter counter;

    public MyHahaComponent(MeterRegistry meterRegistry) {
        counter = meterRegistry.counter("myhaha.hello");
    }

    public int check() {
        return 1;
    }

    public void hello() {
        counter.increment();
        System.out.println("hello");
    }
}
