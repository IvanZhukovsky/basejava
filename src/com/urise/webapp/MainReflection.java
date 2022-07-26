package com.urise.webapp;

import com.urise.webapp.model.Resume;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
        Resume r = new Resume();
        Method method = r.getClass().getDeclaredMethods()[1];
        System.out.println(method);
        System.out.println(method.invoke(r));
    }
}
