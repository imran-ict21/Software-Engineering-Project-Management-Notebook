import annotations.Route;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** Dispatches URL paths to zero-argument @Route methods. */
public class Router {
    public static String dispatch(Container container, String path) {
        for (Object bean : container.getBeans().values()) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                Route route = method.getAnnotation(Route.class);
                if (route != null && route.path().equals(path)) {
                    if (method.getParameterCount() != 0 || method.getReturnType() != String.class) {
                        throw new IllegalStateException("@Route method must have no parameters "
                                + "and return String: " + method.getName());
                    }
                    try {
                        method.setAccessible(true);
                        return (String) method.invoke(bean);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException("Cannot access route: " + path, e);
                    } catch (InvocationTargetException e) {
                        throw new IllegalStateException("Route method failed: " + path,
                                e.getCause());
                    }
                }
            }
        }
        return "404 Not Found: " + path;
    }
}
