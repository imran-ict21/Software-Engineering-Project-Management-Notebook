# Lab_Thread_Counter — Static vs Non-Static Counters in Multithreading

**Name:** Imran Hossen  
**ID:** IT-24063 
**Course code:** ICT-3108

## 1. Files in this directory
| File | Description |
|------|-------------|
| `MyThread.java` | Thread class: static counters (shared) + non-static `count` (per object) |
| `Main.java` | Creates N threads, joins them, calculates and prints the results |
| `handwritten/` | Scans/photos of the handwritten Java code |
| `screenshots/` | Screenshots of every test-case run (safe and unsafe) |
| `RunAll.java`, `results.txt`, `results_table.md` | Script that runs all test cases, raw output, generated tables |

## 2. How to run
```bash
javac Main.java MyThread.java
java Main 10 50000 true     # Experiment A: AtomicLong (thread-safe)
java Main 10 50000 false    # Experiment B: plain long (unsynchronized)
```

## 3. Design
* Each of **N** threads runs one `MyThread` object and does **K** increments.
* **Static counter** — one copy shared by every thread: `AtomicLong safeCount` (Exp. A) or `long unsafeCount` (Exp. B).
* **Non-static counter** — `count`, one private copy per object; summed after `join()`.
* `E = N×K`, `D = |S − I|`, `P = |S − I| / I × 100` (P = 0% if I = 0).

## 4. Test inputs
| Test | Threads N | Increments K | Expected N×K |
|------|-----------|--------------|--------------|
| TC1 | 1   | 1,000  | 1,000 |
| TC2 | 2   | 10,000 | 20,000 |
| TC3 | 5   | 10,000 | 50,000 |
| TC4 | 10  | 50,000 | 500,000 |
| TC5 | 20  | 50,000 | 1,000,000 |
| TC6 | 50  | 50,000 | 2,500,000 |
| TC7 | 100 | 50,000 | 5,000,000 |

## 5. Program outputs
Screenshots: `screenshots/` (e.g. `TC4_safe.png`, `TC4_unsafe_run1.png`). Raw text: `results.txt`.

## 6. Result analysis
_Paste the two tables from your own `results_table.md` here (thread-safe table, then the 5-run unsafe table)._

## 7. Observation and conclusion
_Edit so it matches YOUR numbers._

* **Thread-safe experiment:** S = I = N×K in every test, so D = 0 and P = 0%. This is the control case.
* **Unsynchronized experiment:** `unsafeCount++` is three steps (read, add, write). If two threads read the same value, both write back value+1 and one increment is **lost**, so S ≤ I. The non-static total I is always exactly N×K because no other thread touches an object's own counter.
* **Effect of thread count:** with 1 thread there is no contention, so P = 0%. With more threads, lost updates become possible and often larger. The trend is **not guaranteed to be monotonic**: it depends on thread scheduling, CPU cores, JIT optimisation and run length (a short thread can finish before the next starts, hiding the race).
* **Variation between runs:** the same N gives different S each run because the OS schedules threads differently every time.
* **Conclusion:** `static` vs non-static describes who owns a variable, not whether it is thread-safe. A shared static variable still needs `AtomicLong`, `synchronized` or a lock.

## 8. Answers to analysis questions
1. **Static vs non-static:** a static variable belongs to the class (one shared copy); a non-static variable belongs to each object (own copy per object).
2. **Why threads share the static counter:** it is stored once at class level, so every `MyThread` object uses the same memory location.
3. **Why each thread has its own non-static counter:** each thread runs its own `MyThread` object, and each object has its own `count` field.
4. **Why `join()`:** `start()` returns immediately. Without `join()`, main could read the counters while threads are still running. `join()` makes main wait until each thread finishes.
5. **Why unsynchronized static count can be lower:** `count++` is read–modify–write and not atomic, so two threads can read the same old value and one update overwrites the other (lost update / race condition).
6. **Does more threads always increase the % difference?** No. More threads make races more likely, but the outcome depends on scheduling, cores and timing. Use your table: if a larger N has a smaller P than a smaller N, cite that as evidence.
7. **Why same N gives different results:** the OS scheduler interleaves threads differently each run, so the number of lost updates is non-deterministic.
8. **What `AtomicLong` changes:** `incrementAndGet()` does the read-modify-write as one atomic operation, so no update is lost and S = N×K.
9. **One instance counter shared by all threads:** create a single shared counter object (or `AtomicLong`) and pass it into every thread's constructor, so all threads increment the same instance; it must also be thread-safe (`AtomicLong` or `synchronized`).
