package com.alextim;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyJunit {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        runWith("com.alextim.MyTest");
    }

    public static void runWith(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<TestStatus> generalResult = new ArrayList<>();
        Class<?> cl = Class.forName(className);

        for (Method testMethod : getMethodsByAnnotations(cl, Test.class)) {
            Object instance = instantiate(cl);
            try {
                runMethods(instance, getMethodsByAnnotations(cl, Before.class));
                runMethod(instance, testMethod);
                runMethods(instance, getMethodsByAnnotations(cl, After.class));
                addResultToLog(testMethod, TestStatus.PASSED);
                generalResult.add(TestStatus.PASSED);
            }
            catch (IllegalAccessException e) {
                addResultToLog(testMethod, TestStatus.BROKEN);
                generalResult.add(TestStatus.BROKEN);
            }
            catch (InvocationTargetException e) {
                addResultToLog(testMethod, TestStatus.FAILED);
                generalResult.add(TestStatus.FAILED);
            }
        }
        addGeneralResultToLog(cl, generalResult);
    }

    private static Object instantiate(Class<?> cl) throws IllegalAccessException, InstantiationException {
        return cl.newInstance();
    }

    private static void runMethod(Object instance, Method method, Object... methodParameters)
            throws InvocationTargetException, IllegalAccessException {
        method.invoke(instance, methodParameters);
    }

    private static void runMethods(Object instance, List<Method> methods)
            throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            runMethod(instance, method, null);
        }
    }

    private static List<Method> getMethodsByAnnotations(Class<?> cl, Class<? extends Annotation> annotation) {
        List<Method> methods = new ArrayList<>();
        for (Method method: cl.getDeclaredMethods()) {
            if(method.isAnnotationPresent(annotation))
                methods.add(method);
        }
        return methods;
    }

    private static void addResultToLog(Method method, TestStatus status) {
        String message = "Ending test: [" + method.getName() + "] Status: " + status;
        System.out.println(message);
    }

    private static void addGeneralResultToLog(Class cl, List<TestStatus> status) {
        String message = new StringBuilder("Test Execution for completed. ")
                .append(cl.getName()).append(": ")
                .append(Collections.frequency(status, TestStatus.PASSED)).append(" PASSED, ")
                .append(Collections.frequency(status, TestStatus.BROKEN)).append(" BROKEN, ")
                .append(Collections.frequency(status, TestStatus.FAILED)).append(" FAILED. \n").toString();
        System.out.println(message);
    }

}


