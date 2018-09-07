/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.tmffjtl21.sp5.five;

import java.util.List;

public class Generics6 {
    // T로 쓰는거랑 ? 로 쓰는것의 차이점 ?
    // 둘다 비슷한 개념이고
    // ? 로 사용하는 거를 wildcards ( 뭐라도 들어가도 된다 )
    // 이 오브젝트에 있는 기능만 갖다쓰겠다...
    // 뭐가 오던지 상관없고 Generics 를 받는 List가 가지고 있는 메소드만 사용하겠다.

    public static void main(String[] args) {
        List<?> list;       // List<? extends Object> list;
    }


}
