/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.tmffjtl21.sp5.generics;

import java.util.Arrays;

public class Generics3 {
    // generics 메소드로 선언 리턴타입 앞에 선언
    // bounded type : type parameter를 제한할 수 있다
    static <T extends Comparable<T>> long countGreaterThan(T[] arr, T elem){
        return Arrays.stream(arr).filter(s -> s.compareTo(elem) > 0).count();
    }

    public static void main(String[] args) {
        String[] arr = new String[]{"a", "b", "c", "d" ,"e"};
        System.out.println(countGreaterThan(arr, "c"));
    }
}
