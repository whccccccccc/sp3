package com.atguigu.boot3.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class AppSecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(registry -> {
            registry.requestMatchers("/").permitAll();//1、首页所有人都允许
            registry.anyRequest().authenticated();//剩下的任意请求都需要认证
        });
        httpSecurity.formLogin(httpSecurityFormLoginConfigurer -> {
            httpSecurityFormLoginConfigurer.loginPage("/login").permitAll();//2、自定义登录页面
        });
        DefaultSecurityFilterChain build = httpSecurity.build();
        return build;
    }

    //查询用户详情的
    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails zs = User.withUsername("zhangsan").password(passwordEncoder.encode("123456")).roles("admin", "hr").authorities("file_read", "file_write").build();
        UserDetails ls = User.withUsername("lisi").password(passwordEncoder.encode("123456")).roles("hr").authorities("file_read").build();
        UserDetails ww = User.withUsername("wangwu").password(passwordEncoder.encode("123456")).roles("admin").authorities("file_write", "world_exec").build();
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(zs, ls, ww);
        return inMemoryUserDetailsManager;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
