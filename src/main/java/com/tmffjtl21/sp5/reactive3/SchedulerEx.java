/*
 * Copyright (c) 2018.  by tmffjtl21
 */

package com.tmffjtl21.sp5.reactive3;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Slf4JLoggerFactory;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Toby TV 7회 - 스프링 리액티브 웹 개발(3) - Reactive Streams - Schedulers
 *
 * */


public class SchedulerEx {
    static InternalLogger logger = Slf4JLoggerFactory.getInstance(Schedulers.class);

    public static void main(String[] args) {
        // springboot
        Publisher<Integer> pub = sub -> {
            sub.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {

                    logger.info(Thread.currentThread().getName() + " request()");

                    sub.onNext(1);
                    sub.onNext(2);
                    sub.onNext(3);
                    sub.onNext(4);
                    sub.onNext(5);
                    sub.onComplete();

                    // 위부분을 다른 쓰레드로 작업을 이관하고 메인쓰레드는 빠져나가게 하기 위한 건 : Scheduler
                }

                @Override
                public void cancel() {

                }
            });
        };

        // pub


        // Scheduler Publisher 생성
//        Publisher<Integer> subOnPub = sub -> {
//            ExecutorService es = Executors.newSingleThreadExecutor(new CustomizableThreadFactory(){
//                @Override
//                public String getThreadNamePrefix(){return "subOn- : ";}
//            });
//            es.execute(() -> pub.subscribe(sub));
//        };

        Publisher<Integer> pubOnpub = sub -> {

            pub.subscribe(new Subscriber<Integer>() {

                ExecutorService es = Executors.newSingleThreadExecutor(new CustomizableThreadFactory(){
                    @Override
                    public String getThreadNamePrefix(){return "pubOn- : ";}
                });

                @Override
                public void onSubscribe(Subscription s) {
                    sub.onSubscribe(s);
                }

                @Override
                public void onNext(Integer integer) {
                    es.execute(() -> sub.onNext(integer));
                }

                @Override
                public void onError(Throwable t) {
                    es.execute(() -> sub.onError(t));
                    es.shutdown();
                }

                @Override
                public void onComplete() {
                    es.execute(() -> sub.onComplete());
                    es.shutdown();
                }
            });
        };


        // sub

        pubOnpub.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                logger.info(Thread.currentThread().getName() + "onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                logger.info("onNext:{}", integer);
            }

            @Override
            public void onError(Throwable t) {
                logger.info("onError:{}",t);
            }

            @Override
            public void onComplete() {
                logger.info("onComplete");
//                if (1==1) throw new RuntimeException();
            }
        });

        logger.info(Thread.currentThread().getName() + " Exit");
    }
}