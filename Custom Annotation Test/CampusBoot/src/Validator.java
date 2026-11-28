import annotations.Range;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/** Validates integer fields marked with @Range. */
public class Validator {
    public static List<String> validate(Object obj) {
        List<String> violations = new ArrayList<>();
        if (obj == null) {
            throw new IllegalArgumentException("Object to validate cannot be null.");
        }

        for (Field field : obj.getClass().getDeclaredFields()) {
            Range range = field.getAnnotation(Range.class);
            if (range == null) {
                continue;
            }
            if (field.getType() != int.class && field.getType() != Integer.class) {
                throw new IllegalArgumentException("@Range can only be used on int fields: "
                        + field.getName());
            }

            try {
                field.setAccessible(true);
                int actual = field.getInt(obj);
                if (actual < range.min() || actual > range.max()) {
                    violations.add(field.getName() + " - " + range.message()
                            + " (actual: " + actual + ")");
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Cannot read field: " + field.getName(), e);
            }
        }
        return violations;
    }
}
