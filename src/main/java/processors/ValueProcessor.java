package processors;

import annotations.GetValue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ValueProcessor {
    private Properties properties;
    private Boolean fileExist = true;

    public ValueProcessor(String filePath) {
        properties = new Properties();
        try {
            properties.load(Files.newInputStream(Paths.get(filePath)));
        } catch (IOException e) {
            fileExist = false;
        }
    }

    public void process(Object obj) {
        if (!fileExist) return;
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(GetValue.class)) {
                GetValue annotation = field.getAnnotation(GetValue.class);
                String property = annotation.value();
                String value = properties.getProperty(property);
                if (value != null) {
                    field.setAccessible(true);
                    try {
                        field.set(obj, convert(field.getType(), value));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private Object convert(Class<?> type, String value) {
        if (type == String.class) {
            return value;
        } else if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        } else if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        }
        throw new IllegalArgumentException("Unsupported field type: " + type.getName());
    }
}