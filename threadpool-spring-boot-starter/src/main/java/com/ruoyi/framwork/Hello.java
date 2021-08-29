package com.ruoyi.framwork;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Hello {
    @Bean
//    @ConditionalOnClass(String.class)
    public PrintHello printHello() {
        return new PrintHello();
    }
}
