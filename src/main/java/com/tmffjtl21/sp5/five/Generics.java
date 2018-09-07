/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.tmffjtl21.sp5.five;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Generics {

    // 클래스에 Type 파라미터를 추가했다라는건
    // 메소드의 파라미터는 일반적으로 값을 던지는 것이고,
    // 클래스의 TypeParameter에도 실제로 타입정보를 주면
    static class Hello<T> {     // T -> type parameter
        // T t; // T에 해당하는 자리에 매핑이 되서 필드로 사용되기도 하고
        // T method(T val) {return null;}; // 메소드의 벨류로 사용되기도 하고 // 심지어는 이렇게 사용되기도 한다

    }

    static void print(String value) {
        System.out.println(value);
    }

    public static void main(String[] args) {
//        print("Generics");
//        new Hello<String>();        // type argument
//
//        List<String> list = new ArrayList<>();
// type 을 주지 않으면 컴파일러가 체크를 못하기 때문에 Generics 사용.. 1차적인 목적
//        String s = list.get(0);

        List list = new ArrayList<Integer>();

        List<Integer> ints = Arrays.asList(1,2,3);
        List rawInts = ints;
        @SuppressWarnings("unchecked")
        List<Integer> ints2 = rawInts;
        List<String> strs = rawInts;
//        String str = strs.get(0);
    }
}
