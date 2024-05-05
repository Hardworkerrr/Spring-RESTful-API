package com.example.app.util;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;


@Component
public class EntityUtils {

    private EntityUtils() {

    }

    public static void updateNonNullFields(Object source, Object target) {
        if(!source.getClass().equals(target.getClass())){
            return;
        }
        Class<?> clazz = source.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(source);
                if (value != null) {
                    field.set(target, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
