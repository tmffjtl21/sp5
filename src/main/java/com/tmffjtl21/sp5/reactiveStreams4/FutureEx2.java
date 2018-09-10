/*
 * Copyright (c) 2018.  by tmffjtl21
 */

package com.tmffjtl21.sp5.reactiveStreams4;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

import java.util.Objects;
import java.util.concurrent.*;

// Future 의 get이 블로킹이기때문에 Future 자체를 오브젝트로 만드는 방법 : FutureTask 사용
// Future 작업과 Callback이 하나의 오브젝트에 들어있음
public class FutureEx2 {
    static InternalLogger logger = Slf4JLoggerFactory.getInstance(FutureEx.class);

    interface SuccessCallback {
        void onSuccess(String result);
    }

    // 익셉션을 자연스럽게 Main쓰레드로 넘겨주는 방법이 필요 -> Exception Callback
    interface ExceptionCallback {
        void onError(Throwable t);
    }

    // 비동기 작업이 정상적으로 종료가 됏으면 그때 어떤 작업을 해야하는지 담을수있는 콜백 인터페이스 개발
    public static class CallbackFutureTask extends FutureTask<String> {
        SuccessCallback sc;
        ExceptionCallback ec;

        public CallbackFutureTask(Callable<String> callable, SuccessCallback sc, ExceptionCallback ec) {
            super(callable);
            this.sc = Objects.requireNonNull(sc);   // Objects.requireNonNull : sc가 널이면 널포인트 익셉션을 던져주는 메소드
            this.ec = Objects.requireNonNull(ec);
        }

        // FutureTask 를 이용해서 성공했을 때 실행할 메소드를 만들어줌
        @Override
        protected void done(){
            try {
                // 여기서 get은 현재 쓰레드의 FutureTask 작업에서의 get() 이므로 이 익셉션을 자연스럽게 Main쓰레드로 넘겨주는 방법이 필요
                sc.onSuccess(get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();     // 이걸로 충분하다고 함.
            } catch (ExecutionException e) {
                ec.onError(e.getCause());   // e는 한번 포장된거라 그걸 한번 까서 (getCause) 를 넘겨주면 깔끔함
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        /**이코드에서 마음에 안드는점을 리팩토링 (Future3.java로)*/
        // 비동기 작업을 수행시키는 메카니즘과 관련된 기술적인 코드와 비지니스 로직이 담겨있는 코드와
        // 비동기 작업을 수행해, 쓰레드를 잘 종료 시켜줘 이런 성격이 다른 코드가 짬뽕이 돼있음
        // 이걸 분리해내고 추상화 하는 작업을 스프링을 사용. -> 10년전 기술임

        ExecutorService es = Executors.newCachedThreadPool();

        CallbackFutureTask f = new CallbackFutureTask(()->{
            Thread.sleep(2000);
            // 강제로 예외하나 생성
            if(1==1) throw new RuntimeException("Async ERROR!!!");          // 컴파일러 속이기 ( 1==1 )
            logger.info("Async");
            return "Hello2";
        },
                // 아래가 콜백 메소드
                sc -> System.out.println("Result : " + sc),
                ec-> System.out.println("Error : " + ec.getMessage()));
//        FutureTask<String> f = new FutureTask<String>( () -> {
//            Thread.sleep(2000);
//            logger.info("Async");
//            return "Hello";
//
//            // 결과를 Future를 사용하여 get을 하지 않고 가져오는 방법이있음. -> 익명클래스로 // 대괄호를 붙이면 익명클래스가 됨
//        }) {
//            @Override
//            protected void done(){
//                try{
//                    System.out.println(get());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//            }
//        };

        es.execute(f);
        es.shutdown();
    }
}
