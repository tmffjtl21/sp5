/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.tmffjtl21.sp5;

import java.util.List;

public class TypeToken {

    static class Generic<T> {
        T value;
        void set(T t){}
        T get() { return null; }
    }

    public static void main(String[] args) throws Exception{
        Generic<String> s = new Generic<String>();
    }

    // 뭔가를 리턴하는 간단한 메소드
    // Class
//    static <T> T create(Class<T> clazz) throws Exception{
//        return clazz.newInstance();
//    }

//    public static void main(String[] args) throws Exception{
//        Object o = create(List.class);
//        System.out.println(o.getClass());
//    }
}
// type token
