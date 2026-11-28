import annotations.Range;

public class Student {
    @Range(min = 0, max = 100, message = "Score must be between 0 and 100")
    private int score;

    @Range(min = 1, max = 12)
    private int semester;

    public Student(int score, int semester) {
        this.score = score;
        this.semester = semester;
    }
}
