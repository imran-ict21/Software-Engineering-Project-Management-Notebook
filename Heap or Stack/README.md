# Java Lab: Stack and Heap Memory

**Name:** Al-Shahariar
**Student ID:** IT-24057
**Department:** ICT, MBSTU

This folder has two Java programs. They show where Java stores objects, instance variables and local variables in memory.

## Files

| File | Problem |
|------|---------|
| `StackHeapDemo.java` | Problem 1: Stack and Heap memory with a `Student` class |
| `EmployeeMemoryDemo.java` | Problem 2: Stack frames, heap objects and a local inner class |

## Problem 1: `StackHeapDemo.java`

- `Student` class with the instance variable `age`
- `display()` method with the local variable `marks`
- Creates a `Student` object in `main()` and prints its identity hash code using `System.identityHashCode()`

**Sample input**
```
Age: 20
Marks: 85
```

**Sample output**
```
Student Object Created
Object Identity Hash Code: 1072591677
Instance Variable (age): 20
Local Variable (marks): 85
```

## Problem 2: `EmployeeMemoryDemo.java`

- `Employee` class with the instance variable `salary`
- `display()` method with the local variable `bonus`
- Local inner class `Report` inside `display()`, with a `show()` method
- Prints the identity hash codes of both the `Employee` and `Report` objects

**Sample input**
```
Enter salary: 30000
Enter bonus: 5000
```

**Sample output**
```
Employee Object Created
Employee Identity Hash Code: 1072591677

Inside display() Method:
Salary (Instance Variable): 30000
Bonus (Local Variable): 5000

Report Object Created
Report Identity Hash Code: 1421795058
Report: Employee salary is 30000

Memory Concept:
Employee object -> Heap
Report object -> Heap
salary -> Instance field
bonus -> Local variable in display()
show() -> Executes in its own method frame
```

> The identity hash code changes every time the program runs. It is not the actual memory address.

## Memory Concept

| Item | Stored in |
|------|-----------|
| Objects (`new Student()`, `new Employee()`, `new Report()`) | Heap |
| Instance variables (`age`, `salary`) | Heap, inside the object |
| Local variables (`marks`, `bonus`) | Stack, in the method's frame |
| Object references (`s`, `e`, `r`) | Stack |
| Each method call (`main()`, `display()`, `show()`) | A new stack frame |

## How to Run

You need JDK 8 or later.

```bash
javac StackHeapDemo.java
java StackHeapDemo

javac EmployeeMemoryDemo.java
java EmployeeMemoryDemo
```
