/*
 * Copyright (c) 2018.  by tmffjtl21
 */

package com.tmffjtl21.sp5.reactive6;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

// Mono
// Flux : 데이터가 여러개가 갈수있는것, 넣어서 map을 쓸수있다. httpStream을 지원하려면
// (청크단위로 나눠서 보내는 방식을 쓸려면 Flux를 이용하면 편리함) TEXT_EVENT_STREAM_VALUE
// httpStream : 웹 표준

@SpringBootApplication
@Slf4j
@RestController     // RestFul 방식으로 리턴함 ( JSON )
public class Toby014 {

    @GetMapping("/event/{id}")
    Mono<List<Event>> event(@PathVariable long id){

        List<Event> list = Arrays.asList(new Event(1L, "event1"), new Event(2L, "event2"));

        return Mono.just(list);     // 준비가 된 데이터를 넘길때는 just
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> events(){
//        List<Event> list = Arrays.asList(new Event(1L, "event1"), new Event(2L, "event2"));
//        return Flux.just(new Event(1L, "event1"), new Event(2L, "event2"));

        // Generation 한 10개정도의 이벤트 데이터를 만들어서 할거다. 1번은 Stream을 이용해서  스트림도 데이터를 하나씩 끌어가는 개념

//        return Flux
////                .fromIterable() // list 데이터일때
////                .fromStream(Stream.generate(() -> new Event(System.currentTimeMillis(), "value")))
////                .<Event>generate(sink -> sink.next(new Event(System.currentTimeMillis(), "value"))) // 데이터를 흘려보내는 개념 Flux 내부에 있는 generator
//                // 두번째꺼 . BiFunction을 사용
//                .<Event, Long>generate(() -> 1L, (id, sink) -> {      // 초기값, BiFunction
//                    sink.next(new Event(id, "value" + id));
//                    return id+1;
//                })
//                .delayElements(Duration.ofMillis(1000))
//                .log()
//                .take(10);

            // 두가지를 결합하여 delayElements 의 효과를 볼수있음
//            Flux<Event> es = Flux.<Event, Long>generate(() -> 1L, (id, sink) -> {      // 초기값, BiFunction
//                        sink.next(new Event(id, "value" + id));
//                        return id+1;}).take(10);
//            Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
//
//            return Flux.zip(es, interval).map(tu -> tu.getT1());        // 두개를 묶어서 인터벌을 준다.


            // 위에꺼 변형
            Flux<String> es = Flux.generate( sink -> sink.next("value"));
            Flux<Long> interval = Flux.interval(Duration.ofMillis(50)).filter(s -> s%10 == 0).take(10);

            return Flux.zip(es, interval).map(tu -> new Event(tu.getT2(), tu.getT1() + tu.getT2()));        // 두개를 묶어서 인터벌을 준다.
    }

    public static void main(String[] args) {
        SpringApplication.run(Toby014.class, args);
    }

    @Data
    @AllArgsConstructor
    public static class Event {
        long id;
        String value;
    }
}
