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
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@SpringBootApplication
@EnableAsync    // @Async 어노테이션을 쓸려면 이걸 적용해줘야함
public class Future4 {
    static InternalLogger logger = Slf4JLoggerFactory.getInstance(Future4.class);
    @RestController
    public static class MyController {
//        @GetMapping("/callable")
////        public Callable<String> async() throws InterruptedException {
////            logger.info("callable");
////            return () -> {
////                logger.info("async");
////                Thread.sleep(2000);
////                return "Hello";
////            };
////        }
//        public String async() throws InterruptedException {
//            logger.info("async");
//            Thread.sleep(2000);
//            return "Hello";
//        };

        // DeferredResult Object만 유지해주면 Work Thread를 만들지 않고도
        Queue<DeferredResult<String>> results = new ConcurrentLinkedDeque<>();

        @GetMapping("/dr")
        public DeferredResult<String> callable() throws InterruptedException{
            logger.info("dr");
            DeferredResult<String> dr = new DeferredResult<String>(600000L);
            results.add(dr);
            return dr;
        }

        @GetMapping("/dr/count")
        public String drcount(){
            return String.valueOf(results.size());
        }

        @GetMapping("/dr/event")
        public String drevent(String msg){
            for (DeferredResult<String> dr : results) {
                dr.setResult("Hello " + msg);
                results.remove(dr);
            }
            return "OK";
        }

    }

    public static void main(String[] args) {
//        try(ConfigurableApplicationContext c = SpringApplication.run(Future3.class, args)){
//
//        }
        SpringApplication.run(Future4.class, args);
    }
}
