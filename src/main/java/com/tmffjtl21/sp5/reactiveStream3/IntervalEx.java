/*
 * Copyright (c) 2018.  by tmffjtl21
 */

package com.tmffjtl21.sp5.reactiveStream3;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Slf4JLoggerFactory;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.scheduler.Schedulers;

import java.security.PublicKey;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// operator의 작업 3가지
// 데이터 변환or조작, 상위 퍼블리셔가 하던 일을 중지시킴(take), 스케쥴링(Thread 조작)

public class IntervalEx {
    static InternalLogger logger = Slf4JLoggerFactory.getInstance(IntervalEx.class);

    public static void main(String[] args) {
        Publisher<Integer> pub = sub -> {
            sub.onSubscribe(new Subscription() {
                int no = 0;
                boolean canceled = false;

                @Override
                public void request(long n) {
                    ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
                    exec.scheduleAtFixedRate(()->{
                        if(canceled) {
                            exec.shutdown();
                            return;
                        }
                        sub.onNext(no++);
                    },0,300, TimeUnit.MICROSECONDS);
                }

                @Override
                public void cancel() {
                    canceled = true;
                }
            });
        };

        Publisher<Integer> takePub = sub -> {
            pub.subscribe(new Subscriber<Integer>() {
                int count = 0;
                Subscription subsc;

                @Override
                public void onSubscribe(Subscription s) {
                    subsc = s;
                    sub.onSubscribe(s);
                }

                @Override
                public void onNext(Integer integer) {
                    sub.onNext(integer);
                    if(++count > 5) {
                        subsc.cancel();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    sub.onError(t);
                }

                @Override
                public void onComplete() {
                    sub.onComplete();
                }
            });
        };

        takePub.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                logger.info("onSubscribe");
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

    }
}
