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

public class Generics8 {
    static class A {}
    static class B extends A {}

    static void print(List<? extends Object> list) {}

    public static void main(String[] args) {
        List<B> listB = new ArrayList<B>();
//        List<A> la = listB;
        List<? extends A> la = listB;
        List<? super B> lb = listB; // super는 뒤에 나오는 타입이 wildcards 의 슈퍼타입이어야 된다.

        la.add(null);   // 이것만 가능
    }
}
