/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.tmffjtl21.sp5.generics;

import java.util.Collections;
import java.util.List;

public class Generics5 {
    static <T> void method(T t, List<T> list) {

    }

    public static void main(String[] args) {
//        Generics5.<Integer>method(1, Arrays.asList(1,2,3));
//        List<String> str = new ArrayList<>(); // 다이아몬드 연산자 앞에 Generics를 체크해서 컴파일러가 알수있게 -> 타입추론
//        List<String> c = Collections.emptyList();  // String type의 빈 리스트를 리턴
        List<String> c = Collections.<String>emptyList();   // java 몇버전엔 안돼서 이걸로
    }
}
