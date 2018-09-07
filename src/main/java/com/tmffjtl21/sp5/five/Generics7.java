/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.tmffjtl21.sp5.five;

import java.util.Arrays;
import java.util.List;

public class Generics7 {
    static void printList(List<Object> list) {
        list.forEach(s -> System.out.println(s));
    }

    static void printList2(List<? extends Comparable> list) {
        list.forEach(s -> System.out.println(s));
//        list.forEach(s -> s.compareTo()); // 이런식으로 쓸수있음
    }

    public static void main(String[] args) {
//        둘다 똑같은 결과
//        printList(Arrays.asList(1,2,3));
//        printList2(Arrays.asList(1,2,3));

        List<Integer> list = Arrays.asList(1,2,3);
//        printList(list);    // 오류가 남. 왜냐면 list<Integer>는 list<Object>의 서브타입이 아님
        printList2(list);    // 2는 가능 왜냐면 어떤것이든 상관없는 wildCards 기 때문

    }
}
