package com.tmffjtl21.sp5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
public class Sp5Application {

    public static void main(String[] args) {

        // try with resource 로 테스트용
//        try(ConfigurableApplicationContext c =
//                    SpringApplication.run(Sp5Application.class, args);
//            ){
//        }
        ConfigurableApplicationContext c = SpringApplication.run(Sp5Application.class, args);
    }

    @RestController
    public static class MyController {
        @RequestMapping("/hello")
        // HTTP Request : 3
        // HTTP Response : 3    status code, header, body
        @ResponseBody // 이게 자동으로 붙는다. HTTP Response Body -> return value
        public Mono<String> hello(){
            return Mono.just("<h1>hello Reactive</h1>")
                    .map(s->s.toUpperCase())
                    .publishOn(Schedulers.newSingle("publishOn"))
                    .log();
        };
    }
    // 스프링 부트의 모든 초기화 작업이 끝나고 나서 모든 CommandLineRunner를 찾아서 실행해주는 빈을 생성
    /*@Bean
    public ApplicationRunner run(){
        return new ApplicationRunner(){
            @Override
            public void run(ApplicationArguments args) throws Exception{
                System.out.println("run()...");
            }
        };
    }*/

    // 람다식으로 변경
    @Bean
    public ApplicationRunner run(){
        return (args) -> System.out.println("run()...");
    }
}
