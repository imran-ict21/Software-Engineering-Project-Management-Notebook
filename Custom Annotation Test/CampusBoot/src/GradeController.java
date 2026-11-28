import annotations.Component;
import annotations.Value;
import annotations.Route;

@Component("gradeController")
public class GradeController {
    @Value("app.name")
    private String appName;

    @Value("app.passMark")
    private int passMark;

    @Route(path = "/hello")
    public String hello() {
        return "Welcome to " + appName;
    }

    @Route(path = "/status")
    public String status() {
        return "Pass mark is " + passMark;
    }
}
