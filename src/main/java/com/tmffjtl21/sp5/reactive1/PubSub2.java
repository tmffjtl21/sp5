/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.tmffjtl21.sp5.reactive1;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Toby TV 6회 - 스프링 리액티브 웹 개발(2) - Operators
 *
 * Reactive Streams
 *
 * Publisher : 계속적으로 데이터를 만들어내는 Provider
 * Subscriber : 데이터를 받아서 최종적으로 처리하는쪽,
 *      Observer 페턴처럼 Publisher로부터 데이터를 받겠다고 신청해야함, ( subscribe 한다고 정의 )
 *      Publisher.subscribe(Subscriber)를 사용해서 자기자신을 던짐
 *      onSubscribe(구독이 시작됏어), onNext(데이터가 있으면 넘겨달라 0~무제한까지 허용),
 *      onError, onComplete (베타적이라 둘중에 하나만 발생가능 , 둘중에 하나의 메소드가 호출되는 순간 이 subscription은 끝남 )
 *
 *  Operators
 *  pub -> [Data1] -> mapPub -> [Data2] -> Op2 -> [Data3] -> logSub
 *                      <- subscribe(logSub)
 *                      -> onSubscribe(s)
 *                      -> onNext
 *                      -> onNext
 *                      -> onComplete
 *                      // 아래로 가는건 downStream
 *                      // 위로가는건 upStream
 *                      // 중간에 Operator는 모두 Publisher여야 한다
 *
 *  1. map(d1 -> f -> d2)
 *
 * */

// alt+shift+M ( intellij 메소드 추출 단축키 )
// ctrl+alt+P ( intellij 변수 추출 단축키 )
// alt+enter ( method 자동 생성 )

public class PubSub2 {
    public static void main(String[] args) {
        Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a+1).limit(10).collect(Collectors.toList()));
//        Publisher<String> mapPub = mapPub(pub, s -> "[" + s + "]");
//        Publisher<Integer> sumPub = sumPub(pub);
        Publisher<StringBuilder> reducePub = reducePub(pub, new StringBuilder(), (a, b) -> a.append(b+","));
        reducePub.subscribe(logSub());
    }

    // 처음값으로 함수로 계산 후 다음 값을 넣고 계산 ( 반복 )
    // 1,2,3,4,5
    // 0 -> (0,1) -> 0 + 1 = 1
    // 1 -> (1,2) -> 1 + 2 = 3
    // 3 -> (3,3) -> 3 + 3 = 6
    // 6 ->
    private static<T,R> Publisher<R> reducePub(Publisher<T> pub, R init, BiFunction<R, T, R> biFunction) {
        return s -> pub.subscribe(new DelegateSub<T, R>(s){
            R result = init;

            @Override
            public void onNext(T i) {
                result = biFunction.apply(result, i);
            }

            @Override
            public void onComplete(){
                sub.onNext(result);
                sub.onComplete();
            }
        });
    }

    // 데이터를 다 받고 있다가 다 계산했을 때만 넘기는 Publisher 만들기
    private static<T, R> Publisher<R> sumPub(Publisher<T> pub) {
        return sub -> pub.subscribe(new DelegateSub<T, R>(sub) {
            // 여기서는 onNext로 바로 넘기면 안됨. 계산해야함
            int sum = 0;
            @Override
            public void onNext(T i) {
                sum += (Integer)i;
            }

            @Override
            public void onComplete(){
                sub.onNext(sum);
                sub.onComplete();
            }
        });
    }

    private static <T, R> Publisher<R> mapPub(Publisher<T> pub, Function<T, R> f) {
        return sub -> pub.subscribe(new DelegateSub<T, R>(sub) {
            @Override
            public void onNext(T i){
                sub.onNext(f.apply(i));
            }
        });
    }

    // Publisher로 부터 아래 4개의 메소드를 통해서 데이터를 받거나
    // 예외가 발생하면 Exception이 발생하면 onError를 통해서 Exception object를 넘겨줘야함
    // onComplete는 완료되었다는 호출
    private static<T> Subscriber<T> logSub() {
        return new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe : ");
                s.request(Long.MAX_VALUE);    // 데이터를 얼만큼 줘라고 요청
            }

            @Override
            public void onNext(T t) {
                System.out.println("onNext: " + t);

            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError:" + t);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };
    }

    private static Publisher<Integer> iterPub(List<Integer> collect) {
        return new Publisher<Integer>() {

            // DB에서 데이터를 처리하는 로직으로 이해하면됨
            Iterable<Integer> iter = collect;

            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                // 제일 먼저 해야될건 Subscriber 객체의 onSubscribe 메소드를 호출하여 구현해줘야함.
                // 여기서 Subscription 객체는 Publisher와 Subscriber 사이에서 구독이 한번 일어나는 액션을 담고 있는 객체
                // subscriber가 구독을 할때마다 Subscription의 작업이 한번씩 일어남
                sub.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        try{
                            iter.forEach(s -> sub.onNext(s));
                            sub.onComplete(); // 완료했다는 신호를 줘야함
                        }
                        catch(Throwable t){
                            sub.onError(t);
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };
    }
}
