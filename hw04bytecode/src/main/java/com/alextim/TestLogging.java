package com.alextim;

public class TestLogging {

    @Log
    public void superMethod1(String a) {
        System.out.println("param1 " + a);
    }

    @Log
    public String superMethod2(String a, String b) {
        System.out.println("param2 " + a + " " + b);
        return "result2";
    }

    @Log
    public String superMethod3(String a, String b, String c) {
        System.out.println("param3 " + a + " " + b + " " + c);
        return "result3";
    }

    @Log
    public void superMethod4(float a) {
        System.out.println("param4 " + a);
    }

    @Log
    public int superMethod5(int a, double b) {
        System.out.println("param5 " + a + " " + b);
        return 5;
    }
}
