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

/**
 *  토비의 봄 TV 9회 - 스프링 리엑티브 웹 개발 5부, 비동기 RestTemplate 과 비동기 MVC의 결합
 * */
@SpringBootApplication
@EnableAsync
public class Toby009 {
//    @RestController
    public static class MyController{
        //RestTemplate rt = new RestTemplate();
//        AsyncRestTemplate rt = new AsyncRestTemplate();     // 비동기가 아니라 동기방식으로 다른 쓰레드를 추가로 만들어서 작업을 넘겨줌
        AsyncRestTemplate rt = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1))); // 적절한 쓰레드수는 코어수*2

//        @Autowired
        MyService myService;

        static final String URL1 = "http://localhost:8081/service?req={req}";
        static final String URL2 = "http://localhost:8081/service2?req={req}";

//        @GetMapping("/rest")
//        public  ListenableFuture<ResponseEntity<String>> rest(int idx) {      // 이렇게 쓰지않고 가공할 수 있다
        public  DeferredResult<String> rest(int idx) {
            // 블로킹 메소드임 쓰레드 한개로 테스트 함 server.tomcat.max-threads=1
            // 그래서 아래의 작업을 비동기로 바꿔야함..
            // 그래서 AsyncRestTemplate 으로 바꿨는데 이건 그냥 서버에 100개의 쓰레드를 더 생성하는거라 이것도 비효율적
//            String res = rt.getForObject("http://localhost:8081/service?req={req}", String.class,"hello" + idx);

//            ListenableFuture<ResponseEntity<String>> res = rt.getForEntity("http://localhost:8081/service?req={req}", String.class, "hello" + idx);

            ListenableFuture<ResponseEntity<String>> f1 = rt.getForEntity(URL1, String.class, "hello" + idx);
            DeferredResult<String> dr = new DeferredResult<>(); // 언젠가 가공된 값을 리턴해주는 객체
            f1.addCallback(s->{

                // 콜백 내에서 두번째 서비스 호출
                ListenableFuture<ResponseEntity<String>> f2 = rt.getForEntity(URL2, String.class, s.getBody());
                f2.addCallback(s2->{
//                    dr.setResult(s2.getBody() + "/work");

                    ListenableFuture<String> f3 = myService.work(s2.getBody());
                    f3.addCallback(s3->{
                        dr.setResult(s3);
                    }, e3 ->{
                        dr.setErrorResult(e3.getMessage());
                    });
                }, e2->{
                    dr.setErrorResult(e2.getMessage());
                });
            }, e->{
                dr.setErrorResult(e.getMessage());      // 콜백에서는 예외를 던지거나 이렇게 처리하는게 아니다. 콜백 방식에서는 어느 스택트레이스를 타고 실행되고 있는지 모르므로
                                                                            // 스프링MVC가 받으리라는 보장이 없다.  그래서 순수하게 dr에 에러메세지를 넘겨줌. 에러인 경우에 응답값이 무엇인가. 이렇게 처리
                                                                            // 대응되는 화면에서는 적절히 처리 해야됨.
            });

            return dr;
        }
    }

    @Service
    public static class MyService{
        @Async
        public ListenableFuture<String> work(String req){
            return new AsyncResult<>(req + "/asyncwork");
        }
    }

    // Async를 쓸때에는 아래의 빈을 생성해줘야함. 기본 쓰레드풀을 설정하지 않으면 쓰레드수가 무한대로 늘어난다
    // 기본적으로 core가 다 차면 큐를 채우기 시작하고 그때 max 풀사이즈만큼 늘린다. 그거까지 다 차면 그때 에러가 난다.
    // ThreadPoolTaskExecutor 는 기본적으로 Queue 사이즈가 무한대로 의미없다함.. (???)
    @Bean
    public ThreadPoolTaskExecutor myThreadPool() {
        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
        te.setCorePoolSize(1);
        te.setMaxPoolSize(1);
        te.initialize();
        return te;
    }

    public static void main(String[] args) {
        SpringApplication.run(Toby009.class, args);
    }
}
