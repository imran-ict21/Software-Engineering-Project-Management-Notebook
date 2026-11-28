import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, String> config = new HashMap<>();
        config.put("app.name", "CampusBoot");
        config.put("app.passMark", "40");

        Container container = new Container();
        container.scan(GradeController.class, ReportPrinter.class);
        container.injectValues(config);

        System.out.println("--- Validation ---");
        Student[] students = {
                new Student(120, 5),
                new Student(85, 5),
                new Student(70, 15)
        };

        for (Student student : students) {
            List<String> errors = Validator.validate(student);
            if (errors.isEmpty()) {
                System.out.println("OK");
            } else {
                errors.forEach(error -> System.out.println("VIOLATION: " + error));
            }
        }

        System.out.println("--- Routing ---");
        for (String path : new String[]{"/hello", "/status", "/unknown"}) {
            System.out.println("[Router] " + path + " -> "
                    + Router.dispatch(container, path));
        }
    }
}
