package com.mredrock.cyxbs.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {
    public ReflectUtil() {
    }

    public static Object invokeMethod(String className, String methodName, Class[] cls, Object[] args) throws Exception {
        Class clazz = Class.forName(className);
        return invokeMethod(clazz, (Object) null, methodName, cls, args);
    }

    public static Object invokeMethod(Object object, String methodName, Class[] cls, Object[] args) throws Exception {
        Class clazz = object.getClass();
        return invokeMethod(clazz, object, methodName, cls, args);
    }

    public static Object invokeMethod(Object object, String methodName) throws Exception {
        Class clazz = object.getClass();
        return invokeMethod(clazz, object, methodName, (Class[]) null, (Object[]) null);
    }

    public static Object invokeMethod(Class clazz, Object object, String methodName, Class[] cls, Object[] args) throws Exception {
        Method method;
        if (null == cls) {
            method = clazz.getDeclaredMethod(methodName, new Class[0]);
        } else {
            method = clazz.getDeclaredMethod(methodName, cls);
        }

        method.setAccessible(true);
        return null == args ? method.invoke(object, new Object[0]) : method.invoke(object, args);
    }

    public static Field fieldGetOrg(Object object, String name) throws Exception {
        Field field = object.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    public static Field fieldGetOrg(Object object, Class<?> clazz, String name) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    public static void fieldSet(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    public static void fieldSet(Object object, Class<?> clazz, String fieldName, Object value) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    public static Object fieldGet(Object object, String fieldName) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    public static Object fieldGet(Object object, Class<?> clazz, String fieldName) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    public static Object getStaticFieldValue(Class<?> clazz, String fieldName) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(fieldName);
    }

    public static Object getField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return prepareField(obj.getClass(), fieldName).get(obj);
    }

    public static void setField(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        prepareField(obj.getClass(), fieldName).set(obj, value);
    }

    private static Field prepareField(Class c, String fieldName) throws NoSuchFieldException {
        while (true) {
            if (c != null) {
                Field var3;
                try {
                    Field f = c.getDeclaredField(fieldName);
                    f.setAccessible(true);
                    var3 = f;
                } catch (Exception var7) {
                    continue;
                } finally {
                    c = c.getSuperclass();
                }

                return var3;
            }

            throw new NoSuchFieldException();
        }
    }
}
