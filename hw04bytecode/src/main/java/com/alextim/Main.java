package com.alextim;

public class Main {

    public static void main(String[] args) {
        TestLogging myClass = new TestLogging();
        myClass.superMethod1("a");
        System.out.println(myClass.superMethod2("a", "b"));
        System.out.println(myClass.superMethod3("a", "b", "c"));
        myClass.superMethod4(10.1f);
        System.out.println(myClass.superMethod5(12 , 10.1));
    }
}
