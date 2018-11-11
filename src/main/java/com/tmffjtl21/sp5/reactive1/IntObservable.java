/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.tmffjtl21.sp5.reactive1;
// 1. Duality
// 2. Observer 페턴 ( listener 나 이벤트 방식은 이 페턴을 따른다 )
// 3. Reactive Streams - 표준( 자바를 다루는 회사들이 Reactive 표준을 정하고 가이드를 정하자고 해서 만듦 , Netflix 등등.. )
// Java9 API 안에 들어감 ( 핵심 인터페이스와 규칙등을 설명해줌 )

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// Iterable <---> Observable ( 쌍대성 duality , 에릭 마이어 )
// Pull(땡겨온다) <---> Push(밀어넣어줌)
public class IntObservable{

    // Override method 생성 알트+쉬프트+P

    static class IntObservable2 extends Observable implements Runnable {

        @Override
        public void run() {
            for(int i=1; i<=10; i++){
                setChanged(); // 바뀌었다는걸 호출
                notifyObservers(i); // 바뀐 값 던져줌     // push
                // int i = it.next(); // 대응           // pull
            }
        }
    }

    // DATA method() <-> method(DATA)
    // 장점 여러개의 Observer가 동시에 받을 수 있다.
    // 별개 쓰레드로 동작하는 소스를 손쉽게 만들 수 있다.

    // 여기 Observer 페턴에서 문제점이 있어서 2가지를 제안함
    // 1. Complete ????
    // 2. Error ???

    public static void main(String[] args) {
//        Observable  // Source(이벤트 소스) -> Event/Data -> Observer(관찰자)
        Observer ob = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(Thread.currentThread().getName() + " " + arg);
            }
        } ;

        IntObservable2 io = new IntObservable2();
        io.addObserver(ob);

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(io);

        System.out.println(Thread.currentThread().getName() + "  EXIT");

        es.shutdown();
//        io.run();
    }
}
