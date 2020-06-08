package com.academic.calendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * 入口
 */

@SpringBootApplication
public class AcadCalMapApplication {

    // 解决elasticsearch和redis依赖冲突的问题
    @PostConstruct
    public void init() {
        // 本质解决netty启动冲突的问题
        // 从Netty4Utils.setAvailableProcessors()
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    public static void main(String[] args) {
        SpringApplication.run(AcadCalMapApplication.class, args);
    }

}
