/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.tmffjtl21.sp5.reactive1;

import java.util.Arrays;
import java.util.Iterator;

public class Reactive {

    // 1. Duality
    // 2. Observer 페턴 ( listener 나 이벤트 방식은 이 페턴을 따른다 )
    // 3. Reactive Streams - 표준( 자바를 다루는 회사들이 Reactive 표준을 정하고 가이드를 정하자고 해서 만듦 , Netflix 등등.. )
    // Java9 API 안에 들어감 ( 핵심 인터페이스와 규칙등을 설명해줌 )

    // Iterable <---> Observable ( 쌍대성 duality , 에릭 마이어 )
    // Pull(땡겨온다) <---> Push(밀어넣어줌)

    public static void main(String[] args) {
        // 여러개의 데이터가 있다고 생각
        Iterable<Integer> iter = Arrays.asList(1,2,3,4,5);
        for(Integer i : iter){
            System.out.println(i);
        }
        // 기존방식 : 이걸 한번에 사용을 해야함
        // 리스트로 받지않고 Iterable<Integer> iter 이렇게 받을 수 있다

        // 위의 소스를 데이터를 직접 넣지않고 Iterable을 구현한
        Iterable<Integer> iter2 = () ->
            new Iterator<Integer>() {
                int i =0;
                final static int MAX = 5;
                public boolean hasNext() {
                    return i < MAX;
                }

                public Integer next() {
                    return ++i;
                }
            };
        for(Integer i : iter2){
            System.out.println(i);
        }

        // 아래와 같이 사용가능
        for(Iterator<Integer> it = iter2.iterator(); it.hasNext();){
            System.out.println(it.next());
        }


    }
}
