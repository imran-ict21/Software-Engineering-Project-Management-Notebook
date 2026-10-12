import java.util.Scanner;

// Problem 1: Stack and Heap Memory in Java
class Student {
    int age; // instance variable -> stored inside the object on the Heap

    Student(int age) {
        this.age = age;
    }

    void display(int m) {
        int marks = m; // local variable -> stored in display()'s stack frame
        System.out.println("Instance Variable (age): " + age);
        System.out.println("Local Variable (marks): " + marks);
    }
}

public class StackHeapDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Age: ");
        int age = sc.nextInt();
        System.out.print("Marks: ");
        int marks = sc.nextInt();

        Student s = new Student(age); // reference 's' on Stack, object on Heap
        System.out.println("Student Object Created");
        System.out.println("Object Identity Hash Code: " + System.identityHashCode(s));

        s.display(marks);
        sc.close();
    }
}
