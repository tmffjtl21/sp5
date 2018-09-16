/*
 * Copyright (c) 2018.  by tmffjtl21
 */

package com.tmffjtl21.sp5.reactive5;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

import java.util.concurrent.*;

/**
 *  토비의 봄 TV 11회 - 스프링 리엑티브 웹 개발 6부, CompletableFuture
 * */
public class CFuture {

    static InternalLogger logger = Slf4JLoggerFactory.getInstance(CFuture.class);
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        CompletableFuture<Integer> f = new CompletableFuture<>();    // 비동기 작업의 결과를 만들어내는 기술
//        f.completeExceptionally(new RuntimeException());
//        System.out.println(f.get());

        ExecutorService es = Executors.newFixedThreadPool(10);

        // 명시적으로 비동기 작업의 성공, 실패를 볼수있다. CompletionState : 하나의 비동기 작업을 완료하고 이게 완료가 됐을때 다음작업을 의존적으로 실행할수 있도록 함
        CompletableFuture
//                .runAsync(() -> logger.info("runAsync"))
//                .thenRunAsync(() -> logger.info("thenRun"))
//                .thenRunAsync(() -> logger.info("thenRun"));    // 중간에 예외가 생기면 이후의 Future들은 실행되지 않는다.
                .supplyAsync(() -> {
                    logger.info("supplyAsync");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return 1;
                })
                .thenCompose(s -> {
                    logger.info("thenApply {}", s);
                    return CompletableFuture.completedFuture(s+1);
                })
                .thenApply(s -> {
                    logger.info("thenApply {}", s);
                    return s*3;
                })
                .thenApplyAsync(s -> {                      // Thread 풀의 전략으로 움직이게 할수있다
                    logger.info("thenApply {}", s);
                    return s*3;
                }, es)
                .exceptionally(e -> -10)        // 위의 상황중에 어디서든 예외가 발생하면 -10을 받아서 마지막 처리에 넘기게
                .thenAcceptAsync(s2 -> logger.info("thenAccept {}" , s2), es);

        logger.info("exit");

        ForkJoinPool.commonPool().shutdown();
        ForkJoinPool.commonPool().awaitTermination(5, TimeUnit.SECONDS);
        es.shutdown();
        es.awaitTermination(5, TimeUnit.SECONDS);
    }
}
