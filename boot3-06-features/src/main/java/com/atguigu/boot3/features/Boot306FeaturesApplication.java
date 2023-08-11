package com.atguigu.boot3.features;

import com.atguigu.boot3.robot.starter.robot.RobotAutoConfiguration;
import com.atguigu.boot3.robot.starter.robot.annotation.EnableRobot;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
//@Import(RobotAutoConfiguration.class) //版本1
//@EnableRobot//版本2
//版本3 使用SPI机制
public class Boot306FeaturesApplication {

//	public static void main(String[] args) {
//		SpringApplication.run(Boot306FeaturesApplication.class, args);
//	}

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Boot306FeaturesApplication.class);
        //可以自定义springApplication参数
        springApplication.setBannerMode(Banner.Mode.CONSOLE);//这个配置不优先 以配置文件为准
        springApplication.run(args);
        // 配置优先级 命令行->包外->包内config->包内根目录->代码\
        //profile > application
        // config > 根目录
        //包外  > 包内
        // 命令行 > 所有
        // 外面 大于 里面
    }

}
