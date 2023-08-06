package com.atguigu.boot3.features;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
//测试类必须在主程序所在包或子包
@SpringBootTest//具备测试SpringBoot应用容器中所有组件的功能
class Boot306FeaturesApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("a");
	}

}
