/*
 * Copyright (c) 2018.  by tmffjtl21
 */

package com.tmffjtl21.sp5;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Slf4JLoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.concurrent.Executors;
@SpringBootApplication
@EnableAsync
public class Emitter {
    static InternalLogger logger = Slf4JLoggerFactory.getInstance(Emitter.class);
    @RestController
    public static class MyController {
        @GetMapping("/emitter")
        public ResponseBodyEmitter emitter() throws InterruptedException{
            ResponseBodyEmitter emitter = new ResponseBodyEmitter();

            Executors.newSingleThreadExecutor().submit(() -> {
                for (int i = 1; i <= 50; i++) {
                    try {
                        emitter.send("<p>Stream " + i + "</p>");
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }
                }
            });
            return emitter;
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(Emitter.class, args);
    }
}
