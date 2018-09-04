/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.tmffjtl21.sp5;

import ch.qos.logback.core.pattern.PostCompileProcessor;

import java.util.Arrays;
import java.util.List;

public class Dispatch {
    // runtime 시점이 되지않아도 컴파일 시점에 적용됨 ( static dispatch )

    /*static class Service{
        void run(int number){
            System.out.println("run()");
        }

        void run(String msg){
            System.out.println("run(" + msg + ")");
        }
    }
    public static void main(String[] args){
        new Service().run(1);
        new Service().run("Dispatch");
    }*/

    // 여기서 static의 의미는 파일옮겨다니기 힘들어서 만든거.
    /*static abstract class Service {
        abstract void run();
    }

    static class MyService1 extends Service {
        @Override // 상위 클래스에 구현되어 있든지 없던지 붙여주는걸 권장하는 annotation
        void run(){
            System.out.println("run1");
        }
    }

    static class MyService2 extends Service {
        @Override // 상위 클래스에 구현되어 있든지 없던지 붙여주는걸 권장하는 annotation
        void run(){
            System.out.println("run2");
        }
    }

    public static void main(String[] args) {
//        Service svc = new MyService1();
//
//        // dynamic dispatching ( runtime 시에 svc에 할당된 오브젝트가 뭔지 확인 후 실행됨 )
//        svc.run();  // receiver parameter ( java spec에 나옴 )

        List<Service> svc = Arrays.asList(new MyService1(), new MyService2());
//        svc.forEach(s -> s.run());
        svc.forEach(Service::run); // Method Reference
    }*/


// 001 postOn에 타입을 그냥 구분시켜버림
    // 그것도 좋지않은 방법이라 지움
    // 다형성 두번 적용이 필요함 ( 이중 폴리모피즘 )
    interface Post {
        void postOn(SNS sns);
    }
    static class Text implements Post{
        @Override
        public void postOn(SNS sns) {
            sns.post(this);
//            System.out.println("text -> " + sns.getClass().getSimpleName());
            // instanceof에 if문을 추가하는것은 좋지않다.
            // 다른 클래스가 생기게 되면 각각의 클래스에 다 추가해줘야함
            // 그래서 위로 바꿈. --- 01 코드로
//            if ( sns instanceof Facebook )
//                System.out.println("text - facebook");
//            if ( sns instanceof Twitter )
//                System.out.println("text - twitter");
        }
    }
    static class Picture implements Post{
        @Override
        public void postOn(SNS sns) {
            sns.post(this);
//            System.out.println("picture -> " + sns.getClass().getSimpleName());
//            if ( sns instanceof Facebook )
//                System.out.println("Picture - facebook");
//            if ( sns instanceof Twitter )
//                System.out.println("Picture - twitter");
        }
    }

    interface SNS {
        // 002
        void post(Text post);
        void post(Picture post);
    }

    static class Facebook implements SNS{
        public void post(Text post) {System.out.println("text-facebook");}
        public void post(Picture post) {System.out.println("Picture-facebook");}
    }
    static class Twitter implements SNS{
        public void post(Text post) {System.out.println("text-Twitter");}
        public void post(Picture post) {System.out.println("Picture-Twitter");}
    }
    static class GooglePlus implements SNS{
        public void post(Text post) {System.out.println("text-GooglePlus");}
        public void post(Picture post) {System.out.println("Picture-GooglePlus");}
    }

    public static void main(String[] args){
        List<Post> posts = Arrays.asList(new Text(), new Picture());
        List<SNS> sns = Arrays.asList(new Facebook(), new Twitter(), new GooglePlus());

        posts.forEach( p -> sns.forEach( s -> p.postOn(s)));
    }
}
