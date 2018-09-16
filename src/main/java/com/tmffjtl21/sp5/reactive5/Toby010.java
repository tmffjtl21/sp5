/*
 * Copyright (c) 2018.  by tmffjtl21
 */

package com.tmffjtl21.sp5.reactive5;

import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

/**
 * 토비의 봄 TV 11회 - 스프링 리엑티브 웹 개발 5부, 비동기 RestTemplate 과 비동기 MVC의 결합
 */
@SpringBootApplication
public class Toby010 {
    @RestController
    public static class MyController {
        //RestTemplate rt = new RestTemplate();
//        AsyncRestTemplate rt = new AsyncRestTemplate();     // 비동기가 아니라 동기방식으로 다른 쓰레드를 추가로 만들어서 작업을 넘겨줌
        AsyncRestTemplate rt = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1))); // 적절한 쓰레드수는 코어수*2

        @Autowired
        MyService myService;

        static final String URL1 = "http://localhost:8081/service?req={req}";
        static final String URL2 = "http://localhost:8081/service2?req={req}";

        @GetMapping("/rest")
        public DeferredResult<String> rest(int idx) {

            DeferredResult<String> dr = new DeferredResult<>(); // 언젠가 가공된 값을 리턴해주는 객체
            toCF(rt.getForEntity(URL1, String.class, "hello" + idx))
                    .thenCompose(s -> {
                        if ( 1==1) throw new RuntimeException("ERROR");
                        return toCF(rt.getForEntity(URL2, String.class, s.getBody()));
                    })
                    .thenApplyAsync(s2 -> myService.work(s2.getBody()))
                    .thenAccept(s3 -> dr.setResult(s3))
                    .exceptionally(e -> { dr.setErrorResult(e.getMessage()); return null;});

            return dr;
        }

        <T> CompletableFuture<T> toCF(ListenableFuture<T> listenableFuture) {
            CompletableFuture<T> cf = new CompletableFuture<T>();                   // 비동기 작업의 결과를 담고 있는 객체  장점 : 명시적으로 비동기 작업의 결과를 쓸수있다
            listenableFuture.addCallback(s -> cf.complete(s), e-> cf.completeExceptionally(e));
            return cf;
        }
    }

    @Service
    public static class MyService {
        public String work(String req) {
            return req + "/asyncwork";
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Toby010.class, args);
    }
}
