/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.tmffjtl21.sp5.reactiveStreams1;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Toby TV 5회 - 스프링 리액티브 웹 개발(1) - Publisher , SubScriber
 *
 * */

public class PubSub {
    public static void main(String[] args) throws Exception{
        // Publisher <- 아까 만든 Observable
        // Subscriber <- 아까 만든 Observer
        Iterable<Integer> itr = Arrays.asList(1,2,3,4,5);
        ExecutorService es = Executors.newSingleThreadExecutor();

        Publisher p = new Publisher(){

            @Override
            public void subscribe(Subscriber s) {  // observer 페턴에서는 이게 끝이었으나 Publisher에서는 다시 값을 넘겨줌
                Iterator<Integer> it = itr.iterator();

                s.onSubscribe(new Subscription() {  // 이건 Publisher 와 Subscriber 사이의 중계역할의 인터페이스다 ( 둘 사이의 속도차가 발생했을 때 조절 )
                    // 버퍼를 사용하지 않아서 백프레셔를 쓰면 메모리가 항상 일정한 크기를 유지하게 흘러가도록 한다
                    @Override
                    public void request(long n) {   // 다 넘겨달라고 하면 long의 멕시멈 넘버를 넣고, 한개만 넣을때는 1

                        es.execute(()->{
                            int i = 0;
                            try{
                                while(++i < n){ // 외부변수를 람다식 안에서 수정할 수 없다. 쓰레드 한정
                                    if(it.hasNext()){
                                        s.onNext(it.next());

                                    }else{
                                        s.onComplete();
                                        break;
                                    }
                                }
                            }catch (RuntimeException e){
                                s.onError(e);
                            }
                        });
                    }

                    @Override
                    public void cancel() {
                    }
                });
            }
        };


        Subscriber<Integer> s = new Subscriber<Integer>() {

            Subscription subscription;

            // 필수
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println(Thread.currentThread().getName() + " onSubscription ");
                this.subscription = s;
                this.subscription.request(1);
            }

            int bufferSize = 2;

            // 옵셔널
            @Override
            public void onNext(Integer item) {
                System.out.println(Thread.currentThread().getName() + " onNext " + item);
//                if( --bufferSize <= 0){
                    this.subscription.request(1);
//                }
            }

            // 이걸 하면 onComplete 불가능. 둘중 하나만 호출
            @Override
            public void onError(Throwable t) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        p.subscribe(s);

        // 쓰레드가 종료를 안해서 wait을 걸어주는 부분
        es.awaitTermination(10, TimeUnit.SECONDS);
        es.shutdown();
    }
}
