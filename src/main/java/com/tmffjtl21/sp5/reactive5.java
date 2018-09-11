/*
 * Copyright (c) 2018.  by tmffjtl21
 */

package com.tmffjtl21.sp5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableAsync    // @Async 어노테이션을 쓸려면 이걸 적용해줘야함
public class reactive5 {

    // 별개의 백단의 서비스를 호출하는 상황은 단순히 비동기 서블릿을 호출하는것만으로는 해결이 어렵다. -> 스프링으로 해결 방법을 설명
    // 요청이 많아져서 쓰레드풀이 풀로 차면 레이턴시가 떨어짐 ( 레이턴시 : 응답시간 )

    @RestController
    public static class MyController{
        @GetMapping("/rest")
        public String rest(int idx){
            return "hello";     // view-name => viewResolver를 통해서 view를 찾아서 => hello.jsp    // 근데 이렇게 하면 ResponseBody에 들어감  String으로 보내면 text/html로 날아감
            // 쓰레드 수가 cpu코어수를 넘어서면 컨텍스트 스위칭이 일어남. 블로킹 메소드를 한번 호출하면 최소 2번의 컨텍스트 스위칭이 일어남


        }
    }

    public static void main(String[] args) {SpringApplication.run(Future4.class, args);}
}
