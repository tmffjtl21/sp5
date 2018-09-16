/*
 * Copyright (c) 2018.  by tmffjtl21
 */

package com.tmffjtl21.sp5.reactive4;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Slf4JLoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadTest {
    static InternalLogger logger = Slf4JLoggerFactory.getInstance(LoadTest.class);

    static AtomicInteger counter = new AtomicInteger(0);
    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        ExecutorService es = Executors.newFixedThreadPool(100);

        RestTemplate rt = new RestTemplate();
        String url = "http://localhost:8080/rest?idx={idx}";

        // 쓰레드 동기화 기법
        CyclicBarrier barrier = new CyclicBarrier(100);

        for (int i = 0; i < 100; i++) {
            es.submit(()->{         // callable의 submit은 Exception을 던짐 대신 리턴이 있어야함.
                int idx = counter.addAndGet(1);
                logger.info("Thread {}", idx);

                barrier.await();                            // 쓰레드가 블로킹 됨 . 101이되면 같이 실행됨

                StopWatch sw = new StopWatch();
                sw.start();
                String res = rt.getForObject(url , String.class, idx);
                sw.stop();
                logger.info("Elapsed: {} {} / {} " , idx, sw.getTotalTimeSeconds(), res);
                return null;
            });
        }
        StopWatch main = new StopWatch();
        main.start();

        es.shutdown();
        es.awaitTermination(5, TimeUnit.SECONDS); // 지정된 시간이 타임아웃 걸리기 전이라면 대기작업이 끝날때까지 기다리자

        main.stop();
        logger.info("Total : {}" , main.getTotalTimeSeconds());
    }
}
