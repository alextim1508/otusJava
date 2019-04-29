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
        List<Method> testMethods= getMethodsByAnnotations(cl, Test.class);
        List<Method> beforeMethods = getMethodsByAnnotations(cl, Before.class);
        List<Method> afterMethods = getMethodsByAnnotations(cl, After.class);

        for (Method testMethod : testMethods) {
            Object instance = instantiate(cl);
            try {
                runMethods(instance, beforeMethods);
                runMethod(instance, testMethod);
                runMethods(instance, afterMethods);
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

    private static void addGeneralResultToLog(Class cl, List<TestStatus> testStatuses) {
        String message = new StringBuilder("Test Execution for completed. ")
                .append(cl.getName()).append(": ")
                .append(Collections.frequency(testStatuses, TestStatus.PASSED)).append(" PASSED, ")
                .append(Collections.frequency(testStatuses, TestStatus.BROKEN)).append(" BROKEN, ")
                .append(Collections.frequency(testStatuses, TestStatus.FAILED)).append(" FAILED. \n").toString();
        System.out.println(message);
    }

}


