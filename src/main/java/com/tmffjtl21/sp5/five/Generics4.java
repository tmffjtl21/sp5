/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.tmffjtl21.sp5.five;

import java.util.ArrayList;
import java.util.List;

public class Generics4 {
    public static void main(String[] args) {
        Integer i = 10;
        Number n = i;

        List<Integer> intList = new ArrayList<>();
// complie error 발생 , 이유 : Integer는 Number의 서브타입이 맞지만 List<Integer>는 List<Number>의 서브타입이 아니다.
//        List<Number> numberList = intList;

// 이건 가능 ArrayList 는 List의 서브타입이 맞음
        ArrayList<Integer> arrList = new ArrayList<>();
        List<Integer> intList2 = arrList;
//  => 결론 Generics 타입은 상속관계에 영향을 주지 않음 .



    }
}
