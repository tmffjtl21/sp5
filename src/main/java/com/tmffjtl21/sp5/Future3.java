/*
 * Copyright (c) 2018.  by tmffjtl21
 */

package com.tmffjtl21.sp5;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Slf4JLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@SpringBootApplication
@EnableAsync    // @Async 어노테이션을 쓸려면 이걸 적용해줘야함
public class Future3 {
    static InternalLogger logger = Slf4JLoggerFactory.getInstance(Future3.class);
//    @Component
//    public static class MyServie {
//        @Async ("tp")     // 이거만 적는 순간 비동기 작업이 됌
//        // return 없이 void만 써도 상관없음. 그냥 작업하고 끝내라 .
//        // 작업시간이 긴 작업은 - 끝나면 데이터베이스에 저장 하고 , DB를 조회하는 코드를 수행하게 한다.  or 세션에 Future 를 저장해놓고 꺼내서 쓴다 .
//
//        // ListenableFuture 는 스프링 4.0에 들어간 기술이고
//        // 자바 1.8부터 지원하는 CompletableFuture가 있음... 혁명적
//        public ListenableFuture<String> hello() throws InterruptedException {
//            logger.info("hello()");
//            Thread.sleep(1000);
//            return new AsyncResult<>("Hello") ;
//        }
//    }

    // ThreadPoolTaskExecutor를 구현해놓지 않으면 simpleThreadPool을 사용하여 thread가 무한대로 생성되고 반환도 되지 않으므로 위험하다.
//    @Bean
//    ThreadPoolTaskExecutor tp(){
//        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
//        te.setCorePoolSize(10);   // 쓰레드를 default로 만들어둠 // 0은 무한대
//        te.setMaxPoolSize(100);     // Queue의 쓰레드가 사이즈가 다 찼을 경우 100개 더 만들어주겠다. 총 300개?
//        te.setQueueCapacity(200);    // 지금은 돌려줄 스레드가 없어.
//        te.setThreadNamePrefix("mythread");
//        te.initialize();;
//        return te;
//    };

//    public static void main(String[] args) {
//        try(ConfigurableApplicationContext c = SpringApplication.run(Future3.class, args)){
//
//        }
//    }

//    @Autowired MyServie myServie;
//
//    @Bean
//    ApplicationRunner run() {
//        return args -> {
//            logger.info("run()");
//            ListenableFuture<String> f = myServie.hello();
//            f.addCallback(s-> System.out.println(s), e-> System.out.println(e.getMessage()));
//            logger.info("exit : " + f.isDone());
//        };
//    }
}
