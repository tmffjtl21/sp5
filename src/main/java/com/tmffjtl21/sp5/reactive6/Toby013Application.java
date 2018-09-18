/*
 * Copyright (c) 2018.  by tmffjtl21
 */

package com.tmffjtl21.sp5.reactive6;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

//@SpringBootApplication
//@RestController
@Slf4j
public class Toby013Application {
    @GetMapping("/")
    Mono<String> hello(){
        log.info("post1");

//        String msg = ;
        // just() 는 동기 방식이다.

        String msg = generateHello();

//        Mono m = Mono.fromSupplier(() -> generateHello()).doOnNext(c->log.info(c)).log();
        Mono<String> m = Mono.just(msg).doOnNext(c->log.info(c)).log();
        String msg2 = m.block();        // block()을 쓰면 subscribe(구독)를 한번 한다.

//        m.subscribe();          // subscribe 하는 동안에 블로킹?!
        // Publisher 타입은 Hot type 과 Cold Type 이 있다.  어떤 상황에서도 고정돼있는 상황
        // Cold Type : ( Subscriber가 구독을 할 때마다 Publisher가 가지고있는 데이터를 모두 다 똑같이보내줌 replay )
        // Hot Type : 실시간으로 일어나는 외부의 액션 등 외부 시스템에서 라이브 정보가 날라온다던가
        log.info("post2 " + msg2);
        return m;
    }

    private String generateHello() {
        log.info("method generateHello()");
        return "Hello Mono";
    }

    public static void main(String[] args) {
        SpringApplication.run(Toby013Application.class, args);
    }
}
