/*
 * Copyright (c) 2018.  by tmffjtl21
 */

package com.tmffjtl21.sp5.reactiveStreams4;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Toby TV 8회 - 스프링 리액티브 웹 개발(4) - 비동기 프로그래밍
 *
 * */
public class FutureEx {
    static InternalLogger logger = Slf4JLoggerFactory.getInstance(FutureEx.class);

    // Future   : 작업의 결과를  get으로 받아옴(블로킹)
    // Callback     : 오브젝트를 던져서 끝나고 실행해달라고 함

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newCachedThreadPool();   // 요청할때마다 새로 만들고 남겨놨다가 다음 작업에서 쓰레드 생성없이 씀

        // 비동기로 실행됨.
        // execute() 메소드는 기본적으로 Runnable 을 사용하는데 이건 리턴을 할수가없다.
        // submit() 메소드는 Runnable도 받지만 리턴이 가능. Callable 을 리턴함 ( Callable은 Exception 을 결과에 리턴하기 때문에 Exception처리를 하지 않아도 된다 )
        Future<String> f = es.submit(() -> {
            Thread.sleep(2000);
            logger.info("Async");
            return "Hello";
        });

        System.out.println(f.isDone());     // f가 끝났는지 안끝났는지 non blocking으로 확인해줌
        Thread.sleep(1000);
        logger.info("다른작업2"); // 동시에 처리되어야 할 작업이라고 생각하면 병렬적으로 처리하고 싶을 경우

        // Future의 get은 main쓰레드가 블로킹이 된다.   Future가 블로킹을 하는게 아니라 get 메소드가 블로킹함
        System.out.println(f.get());
        logger.info("Exit");
    }
}
