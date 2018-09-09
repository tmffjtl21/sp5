/*
 * Copyright (c) 2018.  by tmffjtl21
 */

package com.tmffjtl21.sp5.reactiveStream3;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Slf4JLoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FluxScEx {

    static InternalLogger logger = Slf4JLoggerFactory.getInstance(Schedulers.class);

    public static void main(String[] args) throws InterruptedException {
//        Flux.range(1, 10)
//                .publishOn(Schedulers.newSingle("pub"))
//                .log()
////                .subscribeOn(Schedulers.newSingle("sub"))
//                .subscribe(System.out::println);
//
        System.out.println("exit");

        // 숫자를 계속 쏴주는거 interval() 이건 실행이 안되는 이유 : 데몬쓰레드이기 때문
        // 데몬쓰레드 : jvm 이 User 쓰레드가 하나도 없고 데몬쓰레드만 남아있으면 종료시킴
        // 유저쓰레드 :
        Flux.interval(Duration.ofMillis(200))
                .take(10)           // 10개만 받고 끝내버림
                .subscribe(s -> logger.info("onNext : {}" , s));
        TimeUnit.SECONDS.sleep(10);






        // 유저가 만든 쓰레드는 메인 쓰레드가 종료되어도 실행된다. 예제
//        Executors.newSingleThreadExecutor().execute(() -> {
//            try{
//                TimeUnit.SECONDS.sleep(2);
//            }catch (InterruptedException e){
//            }
//            System.out.println("Hello");
//        });
    }
}
