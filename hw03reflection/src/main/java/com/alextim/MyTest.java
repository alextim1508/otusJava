package com.alextim;

public class MyTest {

    public MyTest() {
        System.out.println("MyTest");
    }

    @Before
    public void setUp1() {
        System.out.println("Before 1");
    }

    @Before
    public void setUp2() {
        System.out.println("Before 2");
    }

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        System.out.println("test2");
    }

    @Test
    public void test3() {
        System.out.println("test3"); throw new RuntimeException();
    }

    @Test
    private void test4() {
        System.out.println("test4");
    }

    @After
    public void teadDown1() {
        System.out.println("After 1");
    }

    @After
    public void teadDown2() {
        System.out.println("After 2");
    }
}
