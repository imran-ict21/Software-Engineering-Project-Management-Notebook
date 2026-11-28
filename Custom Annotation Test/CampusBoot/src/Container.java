import annotations.Component;
import annotations.Value;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A tiny IoC container that creates @Component beans and injects @Value fields.
 */
public class Container {
    private final Map<String, Object> beans = new LinkedHashMap<>();

    public void scan(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            if (!clazz.isAnnotationPresent(Component.class)) {
                System.out.println("[Container] Skipped: " + clazz.getSimpleName()
                        + " (no @Component)");
                continue;
            }

            Component component = clazz.getAnnotation(Component.class);
            String beanName = component.value().trim();
            if (beanName.isEmpty()) {
                beanName = Character.toLowerCase(clazz.getSimpleName().charAt(0))
                        + clazz.getSimpleName().substring(1);
            }

            try {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                Object instance = constructor.newInstance();
                beans.put(beanName, instance);
                System.out.println("[Container] Registered: " + beanName
                        + " (" + clazz.getSimpleName() + ")");
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Could not create bean for "
                        + clazz.getName() + ". A no-argument constructor is required.", e);
            }
        }
    }

    public void injectValues(Map<String, String> config) {
        for (Object bean : beans.values()) {
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                Value valueAnnotation = field.getAnnotation(Value.class);
                if (valueAnnotation == null) {
                    continue;
                }

                String key = valueAnnotation.value();
                if (!config.containsKey(key)) {
                    throw new IllegalArgumentException("Missing configuration key: " + key);
                }

                String rawValue = config.get(key);
                try {
                    field.setAccessible(true);
                    if (field.getType() == String.class) {
                        field.set(bean, rawValue);
                    } else if (field.getType() == int.class
                            || field.getType() == Integer.class) {
                        field.set(bean, Integer.parseInt(rawValue));
                    } else {
                        throw new IllegalArgumentException("@Value supports only String and int: "
                                + field.getName());
                    }
                    System.out.println("[Container] Injected: " + key + " -> "
                            + bean.getClass().getSimpleName() + "." + field.getName());
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Cannot inject field: " + field.getName(), e);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Configuration value for '" + key
                            + "' is not a valid integer: " + rawValue, e);
                }
            }
        }
    }

    public Object getBean(String name) {
        return beans.get(name);
    }

    /** Exposes a read-only view for framework components such as Router. */
    public Map<String, Object> getBeans() {
        return java.util.Collections.unmodifiableMap(beans);
    }
}
